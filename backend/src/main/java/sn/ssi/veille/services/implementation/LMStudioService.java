package sn.ssi.veille.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sn.ssi.veille.config.AIConfig;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.services.AIService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class LMStudioService implements AIService {

    private final WebClient webClient;
    private final AIConfig aiConfig;
    private final CategorieRepository categorieRepository;
    private final ObjectMapper objectMapper;

    public LMStudioService(WebClient.Builder webClientBuilder, AIConfig aiConfig,
            CategorieRepository categorieRepository) {

        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(aiConfig.getTimeout()));

        this.webClient = webClientBuilder
                .baseUrl(aiConfig.getUrl())
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
                .build();

        this.aiConfig = aiConfig;
        this.categorieRepository = categorieRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean isAvailable() {
        try {
            // Simple ping to check if LM Studio is up
            // Note: This endpoint might need adjustment depending on LM Studio version
            String modelId = aiConfig.getModel();
            return modelId != null && !modelId.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CompletableFuture<Article> enrichArticle(Article article) {
        if (!isAvailable()) {
            log.warn("Service IA non disponible, enrichissement ignoré pour : {}", article.getTitre());
            return CompletableFuture.completedFuture(article);
        }

        // Advanced Strategy: JSON Mode + CTI Expert Role
        String systemPrompt = """
                You are an expert Cyber Threat Intelligence (CTI) analyst.
                Your task is to extract structured data from security articles.
                Output MUST be raw JSON only. No markdown, no explanations.
                Strict Schema:
                {
                  "summary": "Technical summary in French (max 30 words)",
                  "gravity": "Integer 1-5 (5=Critical CVE/RCE, 1=Info)",
                  "category": "One of [VULNERABILITY, MALWARE, PHISHING, DATA_BREACH, OTHER]",
                  "tags": ["tag1", "tag2", "tag3"]
                }
                """;

        return webClient.post()
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", prompt)),
                        "temperature", 0.1, // Cryogenic temperature for strict classification
                        "max_tokens", 2000,
                        "response_format", Map.of("type", "json_object"))) // Force JSON Mode
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> parseAIResponse(response, article))
                .toFuture()
                .exceptionally(ex -> {
                    log.error("AI Enrichment Failed for '{}': {}", article.getTitre(), ex.getMessage());
                    return article;
                });
    }

    private String buildPrompt(Article article) {
        return String.format("""
                Title: %s
                Content: %s
                """, article.getTitre(),
                article.getContenu() != null
                        ? article.getContenu().substring(0, Math.min(article.getContenu().length(), 1500)) // More
                                                                                                           // context
                                                                                                           // allowed
                        : "");
    }

    private Article parseAIResponse(Map<String, Object> response, Article article) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty())
                return article;

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            // Robust Parsing: Handle optional Markdown blocks
            if (content.contains("```json")) {
                content = content.substring(content.indexOf("```json") + 7);
                if (content.contains("```")) {
                    content = content.substring(0, content.indexOf("```"));
                }
            } else if (content.contains("```")) {
                content = content.substring(content.indexOf("```") + 3);
                if (content.contains("```")) {
                    content = content.substring(0, content.indexOf("```"));
                }
            }

            // Trim whitespace to ensure valid JSON start/end
            content = content.trim();

            AIResult result = objectMapper.readValue(content, AIResult.class);

            // Mapping des résultats
            article.setResume(result.summary);
            article.setTags(result.tags);
            article.setGravite(mapGravity(result.gravity));

            // Gestion Catégorie
            if (result.category != null) {
                String catName = result.category.trim();
                Categorie cat = categorieRepository.findByNomCategorie(catName)
                        .orElseGet(() -> createCategory(catName)); // Création dynamique ou fallback
                article.setCategorieId(cat.getId());
            }

            log.info("Article enrichi par IA : {} (Gravité: {})", article.getTitre(), article.getGravite());

        } catch (Exception e) {
            log.error("Erreur de parsing IA pour l'article '{}': {}", article.getTitre(), e.getMessage());
            // On retourne l'article tel quel en cas d'erreur de parsing
        }
        return article;
    }

    private Gravite mapGravity(int level) {
        return Gravite.fromNiveau(level);
    }

    private Categorie createCategory(String name) {
        // En vrai projet, on éviterait la création sauvage, mais pour le prototype
        // c'est utile
        return categorieRepository.save(Categorie.builder()
                .nomCategorie(name)
                .description("Catégorie générée par IA")
                .couleur("#808080") // Gris par défaut
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build());
    }

    // DTO interne pour le parsing
    private static class AIResult {
        public String summary;
        public String category;
        public int gravity;
        public String[] tags;
    }
}
