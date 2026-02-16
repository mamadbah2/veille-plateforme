package sn.ssi.veille.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API.
 * 
 * <p>
 * Cette configuration définit les métadonnées de l'API et
 * le schéma de sécurité JWT pour Swagger UI.
 * </p>
 * 
 * <p>
 * Accès à la documentation :
 * </p>
 * <ul>
 * <li>Swagger UI : <code>/swagger-ui.html</code></li>
 * <li>OpenAPI JSON : <code>/v3/api-docs</code></li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Horus - API de Veille Cybersécurité", version = "1.0.0", description = """
        API REST pour la plateforme de veille cybersécurité Horus.

        ## Fonctionnalités principales

        - **Authentification** : Inscription, connexion et gestion JWT
        - **Articles** : Consultation, recherche et filtrage des actualités
        - **Favoris** : Sauvegarde d'articles pour lecture ultérieure
        - **Notifications** : Alertes sur les nouvelles menaces
        - **Sources** : Gestion des sources de veille (Admin)
        - **Catégories** : Classification des articles (Admin)
        """, contact = @Contact(name = "Équipe Backend SSI", email = "backend@ssi.sn"), license = @License(name = "Propriétaire - Usage interne SSI", url = "https://ssi.sn/license")), servers = {
        @Server(url = "http://localhost:4040", description = "Serveur de développement"),
        @Server(url = "https://api.veille.ssi.sn", description = "Serveur de production")
})
public class OpenApiConfig {
    // Configuration via annotations uniquement
}
