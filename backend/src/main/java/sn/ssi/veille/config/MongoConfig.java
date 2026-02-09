package sn.ssi.veille.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Override
    protected String getDatabaseName() {
        return "veille";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        // ATTENTION : Remplace le mot de passe ici directement pour tester
        // Pas de variable, pas de properties. On met le lien en DUR.
        // String connectionString = System.getenv("SPRING_DATA_MONGODB_URI");
        
        System.out.println("------------------------------------------------");
        System.out.println("FORCING MONGO CONNECTION TO: " + connectionString);
        System.out.println("------------------------------------------------");

        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();
        
        return MongoClients.create(settings);
    }
}