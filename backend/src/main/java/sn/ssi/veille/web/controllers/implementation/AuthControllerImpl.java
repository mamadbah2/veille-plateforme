package sn.ssi.veille.web.controllers.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.ssi.veille.services.AuthService;
import sn.ssi.veille.web.controllers.AuthController;
import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

	private final AuthService authService;

	@Override
	public ResponseEntity<AuthResponse> register(@Valid RegisterRequest request) {
		AuthResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public ResponseEntity<AuthResponse> login(@Valid LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<AuthResponse> refreshToken(@Valid RefreshTokenRequest request) {
		AuthResponse response = authService.refreshToken(request);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<MessageResponse> logout() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			authService.logout(authentication.getName());
		}
		return ResponseEntity.ok(MessageResponse.success("Déconnexion réussie"));
	}

}