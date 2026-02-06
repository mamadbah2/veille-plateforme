package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;


public interface AuthController {
    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(
        @Valid @RequestBody RegisterRequest request
    );
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(
        @Valid @RequestBody LoginRequest request
    );

    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refreshToken(
        @Valid @RequestBody RefreshTokenRequest request
    );

    @PostMapping("/logout")
    ResponseEntity<MessageResponse> logout();
}
