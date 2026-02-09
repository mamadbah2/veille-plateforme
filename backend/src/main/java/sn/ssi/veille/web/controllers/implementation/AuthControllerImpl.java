package sn.ssi.veille.web.controllers.implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sn.ssi.veille.web.controllers.AuthController;
import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;

@RestController
public class AuthControllerImpl implements AuthController {

	@Override
	public ResponseEntity<AuthResponse> register(@Valid RegisterRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'register'");
	}

	@Override
	public ResponseEntity<AuthResponse> login(@Valid LoginRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'login'");
	}

	@Override
	public ResponseEntity<AuthResponse> refreshToken(@Valid RefreshTokenRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
	}

	@Override
	public ResponseEntity<MessageResponse> logout() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'logout'");
	}

}