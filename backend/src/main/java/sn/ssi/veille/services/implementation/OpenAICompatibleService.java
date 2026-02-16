package sn.ssi.veille.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import sn.ssi.veille.config.AIConfig;
import sn.ssi.veille.config.PromptConfig;
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
public class OpenAICompatibleService implements AIService {

    private final WebClient webClient;
    private final AIConfig aiConfig;
    private final PromptConfig promptConfig;
    private final CategorieRepository categorieRepository;
    private final ObjectMapper objectMapper;

    public OpenAICompatibleService(WebClient.Builder webClientBuilder, AIConfig aiConfig, PromptConfig promptConfig,
            CategorieRepository categorieRepository) {

        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(aiConfig.getTimeout()));

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    // Augmentation de la taille max pour les réponses IA volumineuses
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
                })
                .build();

        this.webClient = webClientBuilder
                .baseUrl(aiConfig.getUrl())
                .defaultHeader("Authorization", "Bearer " + aiConfig.getApiKey())
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();

        this.aiConfig = aiConfig;
        this.promptConfig = promptConfig;
        this.categorieRepository = categorieRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean isAvailable() {
        try {
            // Real ping to check if Provider is up
            return webClient.get()
                    .uri("/v1/models")
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().is2xxSuccessful())
                    .blockOptional(java.time.Duration.ofSeconds(2))
                    .orElse(false);
        } catch (Exception e) {
            log.debug("AI Provider n'est pas accessible : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public CompletableFuture<Article> enrichArticle(Article article) {
        if (!isAvailable()) {
            log.warn("Service IA non disponible, enrichissement ignoré pour : {}", article.getTitre());
            return CompletableFuture.completedFuture(article);
        }

        String systemPrompt = promptConfig.getEnrichment().getSystem();
        String prompt = buildPrompt(article);

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", prompt)),
                        "temperature", 0.1,
                        "max_tokens", 2000))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
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
                        ? article.getContenu().substring(0, Math.min(article.getContenu().length(), 1500))
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
            article.setTags(result.tags);
            article.setGravite(mapGravity(result.gravity));

            // Gestion Catégorie
            if (result.category != null) {
                String catName = result.category.trim();
                Categorie cat = categorieRepository.findByNomCategorie(catName)
                        .orElseGet(() -> createCategory(catName));
                article.setCategorieId(cat.getId());
            }

            log.info("Article enrichi par IA : {} (Gravité: {})", article.getTitre(), article.getGravite());

        } catch (Exception e) {
            log.error("Erreur de parsing IA pour l'article '{}': {}", article.getTitre(), e.getMessage());
        }
        return article;
    }

    private Gravite mapGravity(int level) {
        return Gravite.fromNiveau(level);
    }

    private Categorie createCategory(String name) {
        return categorieRepository.save(Categorie.builder()
                .nomCategorie(name)
                .description("Catégorie générée par IA")
                .couleur("#808080")
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build());
    }

    @Override
    public CompletableFuture<List<Double>> getEmbeddings(String text) {
        if (!isAvailable()) {
            return CompletableFuture.completedFuture(List.of());
        }

        return webClient.post()
                .uri("/v1/embeddings")
                .bodyValue(Map.of(
                        "model",
                        aiConfig.getEmbeddingModel() != null ? aiConfig.getEmbeddingModel() : aiConfig.getModel(),
                        "input", text))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(response -> {
                    List<Double> embedding = parseEmbeddingResponse((Map<String, Object>) response);
                    return embedding;
                })
                .toFuture()
                .exceptionally(ex -> {
                    log.error("Erreur génération embedding: {}", ex.getMessage());
                    return List.of();
                });
    }

    private List<Double> parseEmbeddingResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            if (data != null && !data.isEmpty()) {
                Object embeddingObj = data.get(0).get("embedding");
                if (embeddingObj instanceof List) {
                    return ((List<?>) embeddingObj).stream()
                            .map(obj -> ((Number) obj).doubleValue())
                            .toList();
                }
            }
        } catch (Exception e) {
            log.error("Erreur parsing embeddings: {}", e.getMessage());
        }
        return List.of();
    }

    @Override
    public CompletableFuture<String> cleanContent(String rawText) {
        if (!isAvailable() || rawText == null || rawText.isEmpty()) {
            return CompletableFuture.completedFuture(rawText);
        }

        String systemPrompt = promptConfig.getCleaning().getSystem();

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content",
                                        rawText.substring(0, Math.min(rawText.length(), 6000)))),
                        "temperature", 0.1,
                        "max_tokens", 4000))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(response -> {
                    String cleaned = extractContent(response);
                    return (cleaned != null && !cleaned.isBlank()) ? cleaned : rawText;
                })
                .toFuture()
                .exceptionally(ex -> {
                    log.error("AI Cleaning Failed: {}", ex.getMessage());
                    return rawText;
                });
    }

    @Override
    public CompletableFuture<String> generateSummary(String contenu) {
        if (!isAvailable() || contenu == null || contenu.length() < 200) {
            return CompletableFuture.completedFuture(null);
        }

        String systemPrompt = promptConfig.getSummary().getSystem();

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content",
                                        contenu.substring(0, Math.min(contenu.length(), 4000)))),
                        "temperature", 0.3,
                        "max_tokens", 500))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(response -> {
                    String summary = extractContent(response);
                    return (summary != null && !summary.isBlank()) ? summary : null;
                })
                .toFuture()
                .exceptionally(ex -> {
                    log.error("Génération résumé échouée: {}", ex.getMessage());
                    return null;
                });
    }

    private String extractContent(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                String content = ((String) message.get("content")).trim();
                return content.isEmpty() ? null : content;
            }
        } catch (Exception e) {
            log.error("Error parsing AI cleaning response", e);
        }
        return null;
    }

    @Override
    public CompletableFuture<String> generateStorySynthesis(List<Article> articles) {
        if (!isAvailable() || articles == null || articles.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        StringBuilder articlesContext = new StringBuilder();
        int counter = 1;
        for (Article art : articles) {
            articlesContext.append("Article ").append(counter++).append(":\n")
                    .append("Title: ").append(art.getTitre()).append("\n")
                    .append("Summary: ").append(art.getResume()).append("\n\n");
        }

        String systemPrompt = promptConfig.getClustering().getSystem();

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(Map.of(
                        "model", aiConfig.getModel(),
                        "messages", List.of(
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", articlesContext.toString())),
                        "temperature", 0.3,
                        "max_tokens", 2500))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(response -> {
                    String content = extractContent(response);
                    return cleanJsonBlock(content);
                })
                .toFuture()
                .exceptionally(ex -> {
                    log.error("Erreur synthèse Story: {}", ex.getMessage());
                    return null;
                });
    }

    private String cleanJsonBlock(String content) {
        if (content == null)
            return null;
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
        return content.trim();
    }

    private static class AIResult {
        public String category;
        public int gravity;
        public String[] tags;
    }
}
