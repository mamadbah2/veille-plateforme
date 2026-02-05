package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un utilisateur de la plateforme de veille.
 * Peut être un étudiant SSI ou un administrateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    /**
     * Identifiant unique de l'utilisateur (généré par MongoDB).
     */
    @Id
    private String id;

    /**
     * Adresse email unique de l'utilisateur.
     */
    @Indexed(unique = true)
    private String email;

    /**
     * Nom d'utilisateur unique.
     */
    @Indexed(unique = true)
    private String username;

    /**
     * Mot de passe hashé (BCrypt recommandé).
     */
    private String password;

    /**
     * Rôles de l'utilisateur (ROLE_USER, ROLE_ADMIN).
     */
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /**
     * Indique si le compte est actif.
     */
    @Builder.Default
    private boolean enabled = true;

    /**
     * Indique si le compte n'est pas expiré.
     */
    @Builder.Default
    private boolean accountNonExpired = true;

    /**
     * Indique si le compte n'est pas verrouillé.
     */
    @Builder.Default
    private boolean accountNonLocked = true;

    /**
     * Indique si les credentials ne sont pas expirés.
     */
    @Builder.Default
    private boolean credentialsNonExpired = true;

    /**
     * Date de création du compte.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
