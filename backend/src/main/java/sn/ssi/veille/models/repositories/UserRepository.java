package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.User;

import java.util.Optional;

/**
 * Repository MongoDB pour l'entité User.
 * 
 * <p>Spring Data MongoDB génère automatiquement l'implémentation.</p>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return un Optional contenant l'utilisateur si trouvé
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur
     * @return un Optional contenant l'utilisateur si trouvé
     */
    Optional<User> findByUsername(String username);

    /**
     * Recherche un utilisateur par email ou nom d'utilisateur.
     *
     * @param email l'email de l'utilisateur
     * @param username le nom d'utilisateur
     * @return un Optional contenant l'utilisateur si trouvé
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Vérifie si un utilisateur existe avec cet email.
     *
     * @param email l'email à vérifier
     * @return true si l'email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec ce nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe déjà
     */
    boolean existsByUsername(String username);
}
