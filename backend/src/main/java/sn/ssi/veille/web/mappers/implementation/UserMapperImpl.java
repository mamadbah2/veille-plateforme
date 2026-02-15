package sn.ssi.veille.web.mappers.implementation;

import org.springframework.stereotype.Component;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;
import sn.ssi.veille.web.mappers.UserMapper;
import sn.ssi.veille.models.entities.User;

@Component
public class UserMapperImpl implements UserMapper {

	@Override
	public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getUsername(),
        user.getRoles(),
        user.isEnabled(),
        user.getCreatedAt()
    );
}
	@Override
	public User toEntity(RegisterRequest request) {
	    if (request == null) {
	        throw new IllegalArgumentException("Request cannot be null");
	    }

		User user = new User();
		user.setEmail(request.email());
		user.setUsername(request.username());
		user.setPassword(request.password());
		return user;
	}

	@Override
	public void updateEntity(UpdateUserRequest request, User user) {
		if (request == null || user == null) {
			return;
		}
		if (request.username() != null) {
			user.setUsername(request.username());
		}
		if (request.oldPassword() != null && request.newPassword() != null) {
			if (!user.getPassword().equals(request.oldPassword())) {
				throw new IllegalArgumentException("Ancien mot de passe incorrect");
			}
			user.setPassword(request.newPassword());
		}
	}

}
