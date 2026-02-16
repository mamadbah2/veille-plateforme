package sn.ssi.veille.web.controllers.implementation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sn.ssi.veille.services.UserService;
import sn.ssi.veille.web.controllers.UserController;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.UserResponse;

// Indication d'une classe de contrôleur REST
@RestController

// Indication d'un composant final (ne peut pas être injecté)
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    /*
    Les méthodes retournent une réponse HTTP :
    - OK (200) : pour les succès
    - NOT_FOUND (404) : pour les utilisateurs non trouvés
    - BAD_REQUEST (400) : pour les requêtes invalides
    - INTERNAL_SERVER_ERROR (500) : pour les erreurs serveur
    */
    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    public ResponseEntity<UserResponse> updateCurrentUser(UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<MessageResponse> disableUser(String id) {
        userService.disableUser(id);
        return ResponseEntity.ok(MessageResponse.success("Utilisateur désactivé avec succès"));
    }

    @Override
    public ResponseEntity<MessageResponse> enableUser(String id) {
        userService.enableUser(id);
        return ResponseEntity.ok(MessageResponse.success("Utilisateur activé avec succès"));
    }

    @Override
    public ResponseEntity<Void> deleteUser(String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
