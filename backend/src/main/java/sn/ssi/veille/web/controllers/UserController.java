package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.UserResponse;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserController {
    @GetMapping("/me")
    ResponseEntity<UserResponse> getCurrentUser();

    @PutMapping("/me")
    ResponseEntity<UserResponse> updateCurrentUser(
        @Valid @RequestBody UpdateUserRequest request
    );

    @GetMapping
    ResponseEntity<List<UserResponse>> getAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(
        @PathVariable String id
    );

    @PatchMapping("/{id}/disable")
    ResponseEntity<MessageResponse> disableUser(
        @PathVariable String id
    );

    @PatchMapping("/{id}/enable")
    ResponseEntity<MessageResponse> enableUser(
        @PathVariable String id
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(
        @PathVariable String id
    );
}
