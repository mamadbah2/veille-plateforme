package sn.ssi.veille.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.lmstudio")
@Data
public class AIConfig {
    private String url;
    private String model;
    private String embeddingModel;
    private Long timeout;
}
