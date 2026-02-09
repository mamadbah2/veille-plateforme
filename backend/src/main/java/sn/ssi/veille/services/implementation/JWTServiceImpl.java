package sn.ssi.veille.services.implementation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import lombok.RequiredArgsConstructor;
import sn.ssi.veille.services.JWTService;

@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
    private final JwtEncoder jwtEncoder;
    
    @Override
    public Instant getExpirationTime() {
        return Instant.now().plus(120, ChronoUnit.MINUTES);
    }

    @Override
    public String generateToken(Authentication authentication, String userIdentifier) {
        Instant instant = Instant.now();
        String formated_authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(120, ChronoUnit.MINUTES))
                .issuer("user-service")
                .claim("authorities", formated_authorities)
                .claim("identifier", userIdentifier)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}