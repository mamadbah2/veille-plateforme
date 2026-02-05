package sn.ssi.veille.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration MongoDB avec auditing activé.
 * 
 * <p>Active les annotations @CreatedDate et @LastModifiedDate.</p>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /**
     * Listener pour la validation des entités avant persistance.
     *
     * @param factory le validateur
     * @return le listener de validation
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    /**
     * Factory pour le validateur de beans.
     *
     * @return le validateur factory
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
