package com.example.bookmark.api;

import com.example.bookmark.DataInitializer;
import com.example.bookmark.service.User;
import com.example.bookmark.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
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
}
