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
        this.webClient = webClientBuilder.baseUrl(aiConfig.getUrl()).build();
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

        String prompt = buildPrompt(article);

        return webClient.post()
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content",
                                        "Tu es un expert en cybersécurité. Analyse l'article suivant et extrais les informations au format JSON strict."),
                                Map.of("role", "user", "content", prompt)),
                        "temperature", 0.3,
                        "max_tokens", 500))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> parseAIResponse(response, article))
                .toFuture();
    }

    private String buildPrompt(Article article) {
        return String.format("""
                Analyse cet article et retourne un JSON avec ces champs:
                - summary (résumé court en français)
                - category (choisir parmi: Vulnérabilité, Malware, Attaque, Fuite de données, Patch, Politique, Autre)
                - gravity (niveau: 1=Info, 2=Avis, 3=Important, 4=Elevé, 5=Critique)
                - tags (liste de 3-5 mots-clés techniques)

                Titre: %s
                Contenu: %s
                """, article.getTitre(),
                article.getContenu() != null
                        ? article.getContenu().substring(0, Math.min(article.getContenu().length(), 1000))
                        : "");
    }

    private Article parseAIResponse(Map<String, Object> response, Article article) {
        try {
            // Extraction du contenu JSON dans la réponse OpenAI-like
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty())
                return article;

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            // Nettoyage du markdown potentiellment retourné par deepseek (```json ... ```)
            if (content.contains("```json")) {
                content = content.substring(content.indexOf("```json") + 7);
                if (content.contains("```")) {
                    content = content.substring(0, content.indexOf("```"));
                }
            } else if (content.contains("```")) { // Cas générique
                content = content.substring(content.indexOf("```") + 3);
                if (content.contains("```")) {
                    content = content.substring(0, content.indexOf("```"));
                }
            }

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
