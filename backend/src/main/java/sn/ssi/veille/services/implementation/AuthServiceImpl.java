package sn.ssi.veille.services.implementation;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sn.ssi.veille.exceptions.UserNotFoundException;
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

        throw new UnsupportedOperationException(
            "Unimplemented method 'register'"
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
                AuthResponse authResponse = new AuthResponse(
                    jwtService.generateToken(
                        authentication,
                        request.identifier()
                    ),
                    "JWT",
                    jwtService.getExpirationTime().toEpochMilli(), // Je suis pas trop sur de ca quand meme.
                    userMapper.toResponse(
                        userRepository
                            .findByEmail(request.identifier())
                            .orElseThrow(() ->
                                new UserNotFoundException("User not found")
                            )
                    )
                );
                return authResponse;
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            System.out.println(request);
            System.out.println(Arrays.stream(e.getStackTrace()).toList());
            throw new AuthenticationCredentialsNotFoundException(
                "Invalid username or password"
            );
        }
        return null;
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'refreshToken'"
        );
    }

    @Override
    public void logout(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'logout'"
        );
    }

    @Override
    public boolean isTokenValid(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'isTokenValid'"
        );
    }
}
