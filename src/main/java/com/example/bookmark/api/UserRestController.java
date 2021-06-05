package com.example.bookmark.api;

import com.example.bookmark.service.User;
import com.example.bookmark.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Retrieves list of users",
            tags = {"User-API"}
    )
    @ResponseStatus(OK)
    @GetMapping
    List<User> findAllUsers() {
        return userService.findAll();
    }

    @Operation(
            summary = "Register new user",
            tags = {"User-API"}
    )
    @ResponseStatus(CREATED)
    @PostMapping
    User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(
            summary = "Change current password of user",
            tags = {"User-API"}
    )
    @ResponseStatus(OK)
    @PostMapping("/{userid}/changepassword")
    void changePassword(@PathVariable("userid") UUID userIdentifier, @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userIdentifier, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
    }

    @Operation(
            summary = "Get user specified by identifier",
            tags = {"User-API"}
    )
    @GetMapping("/{userid}")
    ResponseEntity<User> getUser(@PathVariable("userid") UUID userIdentifier) {
        return userService.findByIdentifier(userIdentifier).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete user specified by identifier",
            tags = {"User-API"}
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{userid}")
    void deleteUser(@PathVariable("userid") UUID userIdentifier) {
        userService.deleteUserByIdentifier(userIdentifier);
    }

    @Operation(
            summary = "Get current authenticated user",
            tags = {"User-API"}
    )
    @ResponseStatus(OK)
    @GetMapping("/me")
    User currentUser(@AuthenticationPrincipal User user) {
        return user;
    }

}
