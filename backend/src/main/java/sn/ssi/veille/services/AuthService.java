package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String userId);

    boolean isTokenValid(String token);
}
