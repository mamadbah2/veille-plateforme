package sn.ssi.veille.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.models.repositories.UserRepository;
import sn.ssi.veille.services.UserService;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;
import sn.ssi.veille.web.mappers.UserMapper;

// Indication à Spring que c'est un composant de service
@Service
// Indication d'un constructeur pour les variables
@RequiredArgsConstructor
// Indication à Spring que les méthodes de cette classe doivent être transactionnelles
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository; // Interaction avec la base de données
    private final UserMapper userMapper; // Conversion entre entités en DTOs
    private final PasswordEncoder passwordEncoder; // Hashage des mots de passe avant stockage

    /*
    getUserById(String id) :
    Cherche un utilisateur par son ID via userRepository.findById(id).
    Si trouvé, le convertit en réponse via userMapper.
    Sinon, lance une exception (orElseThrow).
    */
    @Override
    public UserResponse getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    /*
    getUserByEmail(String email) :
    Cherche un utilisateur par son email via userRepository.findByEmail(email).
    Si trouvé, le convertit en réponse via userMapper.
    Sinon, lance une exception (orElseThrow).
    */
    @Override
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + email));
    }

    /*
    getCurrentUser() :
    Récupère l'utilisateur courant via SecurityContextHolder.
    Utilise getUserByEmail pour obtenir le détail de l'utilisateur.
    */
    @Override
    public UserResponse getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    /*
    updateCurrentUser(UpdateUserRequest request) :
    Met à jour l'utilisateur courant.
    Vérifie l'ancien mot de passe.
    Si le nouveau mot de passe est fourni, le hashage est effectué.
    Mise à jour de l'utilisateur via userRepository.save(user).
    Conversion en réponse via userMapper.
    */
    @Override
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur courant non trouvé"));

        if (request.oldPassword() != null && !request.oldPassword().isEmpty()) {
            if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
                throw new RuntimeException("Ancien mot de passe incorrect");
            }
            if (request.newPassword() != null && !request.newPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.newPassword()));
            }
        }

        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    /*
    getAllUsers() :
    Retourne la liste de tous les utilisateurs.
    Utilise userRepository.findAll() pour obtenir tous les utilisateurs.
    Convertit chaque utilisateur en réponse via userMapper.
    */
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /*
    disableUser(String userId) :
    Désactive un utilisateur.
    Trouve l'utilisateur par son ID via userRepository.findById(userId).
    Si trouvé, met son statut enabled à false.
    Sauvegarde l'utilisateur via userRepository.save(user).
    */
    @Override
    public void disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    /*
    enableUser(String userId) :
    Active un utilisateur.
    Trouve l'utilisateur par son ID via userRepository.findById(userId).
    Si trouvé, met son statut enabled à true.
    Sauvegarde l'utilisateur via userRepository.save(user).
    */
    @Override
    public void enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /*
    deleteUser(String userId) :
    Supprime un utilisateur.
    Vérifie si l'utilisateur existe via userRepository.existsById(userId).
    Si trouvé, supprime l'utilisateur via userRepository.deleteById(userId).
    */
    @Override
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(userId);
    }
}
