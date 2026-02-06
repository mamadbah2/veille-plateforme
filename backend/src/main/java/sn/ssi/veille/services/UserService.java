package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserById(String id);

    UserResponse getUserByEmail(String email);

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UpdateUserRequest request);

    List<UserResponse> getAllUsers();

    void disableUser(String userId);

    void enableUser(String userId);

    void deleteUser(String userId);
}
