/*package sn.ssi.veille.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.models.repositories.UserRepository;
import sn.ssi.veille.services.UserService;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================================");
        System.out.println("          DÉBUT DU TEST MANUEL UTILISATEUR       ");
        System.out.println("=================================================");

        System.out.println("[INFO] Connexion à MongoDB: " + mongoTemplate.getDb().getName());

        // Nettoyage de l'utilisateur de test s'il existe déjà
        userRepository.findByEmail("test@ssi.sn").ifPresent(userRepository::delete);
        System.out.println("[INFO] Utilisateur de test précédent nettoyé (si existant).");

        // Création directe d'un utilisateur (car AuthService n'est pas implémenté)
        User user = User.builder()
                .username("test_user")
                .email("test@ssi.sn")
                .password(passwordEncoder.encode("password123"))
                .enabled(true)
                .build();
        userRepository.save(user);
        System.out.println("[OK] Utilisateur de test créé avec ID: " + user.getId());

        // Simuler l'authentification (nécessaire pour getCurrentUser)
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test@ssi.sn", null, List.of()));
        System.out.println("[INFO] Authentification simulée pour: test@ssi.sn");

        // Test getUserById
        try {
            UserResponse response = userService.getUserById(user.getId());
            System.out.println("[TEST 1 - getUserById] SUCCÈS - Email: " + response.email());
        } catch (Exception e) {
            System.err.println("[TEST 1 - getUserById] ÉCHEC: " + e.getMessage());
        }

        // Test getCurrentUser
        try {
            UserResponse current = userService.getCurrentUser();
            System.out.println("[TEST 2 - getCurrentUser] SUCCÈS - Username: " + current.username());
        } catch (Exception e) {
            System.err.println("[TEST 2 - getCurrentUser] ÉCHEC: " + e.getMessage());
        }

        // Test updateCurrentUser
        try {
            UpdateUserRequest updateRequest = new UpdateUserRequest("updated_user", "password123", "newPassword456");
            UserResponse updated = userService.updateCurrentUser(updateRequest);
            System.out.println("[TEST 3 - updateCurrentUser] SUCCÈS - Nouveau Username: " + updated.username());
        } catch (Exception e) {
            System.err.println("[TEST 3 - updateCurrentUser] ÉCHEC: " + e.getMessage());
        }

        // Test getAllUsers
        try {
            List<UserResponse> users = userService.getAllUsers();
            System.out.println("[TEST 4 - getAllUsers] SUCCÈS - Nombre d'utilisateurs: " + users.size());
        } catch (Exception e) {
            System.err.println("[TEST 4 - getAllUsers] ÉCHEC: " + e.getMessage());
        }

        // Test disableUser
        try {
            userService.disableUser(user.getId());
            // Vérification directe
            User disabledUser = userRepository.findById(user.getId()).orElseThrow();
            System.out.println("[TEST 5 - disableUser] SUCCÈS - Enabled: " + disabledUser.isEnabled());
        } catch (Exception e) {
            System.err.println("[TEST 5 - disableUser] ÉCHEC: " + e.getMessage());
        }

        System.out.println("=================================================");
        System.out.println("           FIN DU TEST MANUEL UTILISATEUR        ");
        System.out.println("=================================================");
    }
}
*/