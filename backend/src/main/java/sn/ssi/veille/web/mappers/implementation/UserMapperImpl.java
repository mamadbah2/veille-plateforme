package sn.ssi.veille.web.mappers.implementation;

import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;
import sn.ssi.veille.web.mappers.UserMapper;
import sn.ssi.veille.models.entities.User;

public class UserMapperImpl implements UserMapper {

	@Override
	public UserResponse toResponse(User user) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toResponse'");
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
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
	}

}
