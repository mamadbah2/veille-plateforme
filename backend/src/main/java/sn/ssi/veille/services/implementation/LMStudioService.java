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
            // Real ping to check if LM Studio is up and responding
            return webClient.get()
                    .uri("/models")
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().is2xxSuccessful())
                    .blockOptional(java.time.Duration.ofSeconds(2))
                    .orElse(false);
        } catch (Exception e) {
            log.debug("LM Studio n'est pas accessible : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public CompletableFuture<Article> enrichArticle(Article article) {
        if (!isAvailable()) {
            log.warn("Service IA non disponible, enrichissement ignoré pour : {}", article.getTitre());
            return CompletableFuture.completedFuture(article);
        }

        // Universal Prompt (IT Watch / Veille Informatique)
        String systemPrompt = """
                You are an expert IT Watch analyst (Veille Informatique).
                Analyze the provided article and categorize it.

                RESPONSE FORMAT MUST BE CLEAN JSON ONLY.
                Strict Schema:
                {
                  "summary": "Technical summary in French (max 30 words)",
                  "gravity": "Integer 1-5 (5=Critical/Breaking Change, 1=Info/Release)",
                  "category": "One of [PROGRAMMING, DATA_SCIENCE, CYBERSECURITY, DEVOPS, TECHNOLOGY]",
                  "tags": ["tag1", "tag2", "tag3", "tag4", "tag5"]
                }

                GUIDELINES FOR CLASSIFICATION:
                1. PROGRAMMING: Software Engineering, Languages (Java, Python, Rust...), Web, Mobile, Architecture.
                   - Tags: Software Architecture, Clean Code, Web Development, Mobile, Java, Python, JavaScript, Rust, Go, Algorithms
                2. DATA_SCIENCE: AI, Machine Learning, Deep Learning, Big Data.
                   - Tags: Artificial Intelligence, Machine Learning, Deep Learning, LLMs, NLP, Computer Vision, Data Engineering, Big Data
                3. CYBERSECURITY: Security, Hacks, Vulnerabilities, Crypto.
                   - Tags: Vulnerability, Malware, Ransomware, Network Security, Cryptography, Hacking, Privacy, OSINT, AppSec
                4. DEVOPS: Cloud, Infrastructure, CI/CD, Containers.
                   - Tags: Cloud Computing, AWS, Azure, Google Cloud, Kubernetes, Docker, CI/CD, Linux, SRE
                5. TECHNOLOGY: General Tech, Business, Startups, Hardware.
                   - Tags: Startup, Big Tech, Hardware, Gadgets, Innovation, Tech Policy, Apple, Microsoft, Google
                """;

        String prompt = buildPrompt(article);

        return webClient.post()
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", prompt)),
                        "temperature", 0.1, // Cryogenic temperature for strict classification
                        "max_tokens", 2000))
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
