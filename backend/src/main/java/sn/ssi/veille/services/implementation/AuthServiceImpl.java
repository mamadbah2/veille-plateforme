package sn.ssi.veille.services.implementation;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sn.ssi.veille.exceptions.EmailAlreadyExistsException;
import sn.ssi.veille.exceptions.UsernameAlreadyExistsException;
import sn.ssi.veille.exceptions.UserNotFoundException;
import sn.ssi.veille.models.entities.Role;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.models.repositories.UserRepository;
import sn.ssi.veille.services.AuthService;
import sn.ssi.veille.services.JWTService;
import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;
import sn.ssi.veille.web.mappers.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
    User user = userMapper.toEntity(request);
    userRepository.save(user);

    // Générer le token JWT pour l'utilisateur
    String accessToken = jwtService.generateToken(user);
    
    // Obtenir la durée d'expiration du token
    long expiresIn = jwtService.getExpirationTime();
    
    // Convertir User en UserResponse
    UserResponse userResponse = userMapper.toResponse(user);
    
    // Retourner la réponse d'authentification
    return new AuthResponse(
        accessToken,
        "Bearer",
        expiresIn,
        userResponse
    );
}
    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.identifier(),
                    request.password()
                )
            );
            
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(request.identifier())
                    .or(() -> userRepository.findByUsername(request.identifier()))
                    .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
                
                String token = jwtService.generateToken(authentication, user.getId());
                
                return new AuthResponse(
                    token,
                    "Bearer",
                    jwtService.getExpirationTime().toEpochMilli(),
                    userMapper.toResponse(user)
                );
            }
        } catch (AuthenticationException e) {
            throw new AuthenticationCredentialsNotFoundException(
                "Identifiant ou mot de passe invalide"
            );
        }
        return null;
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // Pour l'instant, le refresh token n'est pas implémenté
        // On pourrait utiliser un système de refresh token stocké en base
        throw new UnsupportedOperationException(
            "La fonctionnalité de refresh token n'est pas encore implémentée"
        );
    }

    @Override
    public void logout(String userId) {
        // Avec JWT stateless, le logout côté serveur n'est pas nécessaire
        // Le client doit simplement supprimer le token
        // On pourrait implémenter une blacklist de tokens si nécessaire
    }

    @Override
    public boolean isTokenValid(String token) {
        // La validation du token est gérée par Spring Security OAuth2 Resource Server
        // Cette méthode peut être utilisée pour des vérifications supplémentaires
        return token != null && !token.isEmpty();
    }
}
