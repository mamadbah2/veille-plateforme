package sn.ssi.veille.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.provider")
@Data
public class AIConfig {
    /**
     * Type of provider: "openai" (default), "anthropic", "gemini", "lmstudio"
     */
    private String type = "openai";

    private String url;
    private String apiKey;
    private String model;
    private String embeddingModel;
    private Long timeout = 60000L;
}
