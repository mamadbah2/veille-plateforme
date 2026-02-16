package sn.ssi.veille.services.implementation;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sn.ssi.veille.models.entities.Role;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.models.repositories.UserRepository;
import sn.ssi.veille.services.JWTService;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Value("${application.frontend.url:http://localhost:4200/login-success}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        // Extraction de l'email selon le provider
        final String userEmail = extractEmail(oauthToken, oAuth2User);

        // 1. Sauvegarder ou récupérer l'utilisateur
        User user = userRepository.findByEmail(userEmail)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(userEmail)
                            .username(userEmail)
                            .password("")
                            .roles(Set.of(Role.ROLE_USER))
                            .enabled(true)
                            .accountNonExpired(true)
                            .accountNonLocked(true)
                            .credentialsNonExpired(true)
                            .build();
                    return userRepository.save(newUser);
                });

        // 2. Générer le JWT avec les rôles de la DB (pas ceux de OAuth2)
        // Ceci arrive au cas ou il aurait d'autres rôles que ROLE_USER dans la DB
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
        
        Authentication dbAuthentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, authorities);
        
        String token = jwtService.generateToken(dbAuthentication, user.getId());

        // 3. Rediriger vers le Frontend avec le token
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    /**
     * Extrait l'email de l'utilisateur OAuth2 selon le provider (Google/GitHub)
     */
    private String extractEmail(OAuth2AuthenticationToken oauthToken, OAuth2User oAuth2User) {
        if ("github".equals(oauthToken.getAuthorizedClientRegistrationId())) {
            String email = oAuth2User.getAttribute("email");
            String login = oAuth2User.getAttribute("login");
            // Si l'email est privé sur GitHub, utiliser le login comme fallback
            return email != null ? email : login + "@github.com";
        }
        // Google (et autres providers par défaut)
        return oAuth2User.getAttribute("email");
    }

}
