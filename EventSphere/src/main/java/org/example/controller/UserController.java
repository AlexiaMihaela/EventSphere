package org.example.controller;

import jakarta.validation.Valid;
import org.example.controller.dto.CreateUserRequest;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody CreateUserRequest req) {
        return userService.create(req);
    }
}
