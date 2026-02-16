package sn.ssi.veille.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "prompts")
@PropertySource(value = "classpath:prompts.properties", encoding = "UTF-8")
@Data
public class PromptConfig {

    // Nested classes to match properties structure: prompts.enrichment.system
    private Enrichment enrichment = new Enrichment();
    private Cleaning cleaning = new Cleaning();
    private Summary summary = new Summary();
    private Clustering clustering = new Clustering();

    @Data
    public static class Enrichment {
        private String system;
    }

    @Data
    public static class Cleaning {
        private String system;
    }

    @Data
    public static class Summary {
        private String system;
    }

    @Data
    public static class Clustering {
        private String system;
    }
}
