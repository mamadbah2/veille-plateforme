package sn.ssi.veille.services;

import java.time.Instant;

import org.springframework.security.core.Authentication;

public interface JWTService {
    String generateToken(Authentication authentication, String userID);
    Instant getExpirationTime();
}