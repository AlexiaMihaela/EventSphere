package org.example.service;

import org.example.controller.dto.CreateUserRequest;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(CreateUserRequest req) {
        userRepository.findByEmailIgnoreCase(req.email)
                .ifPresent(u -> { throw new IllegalArgumentException("Email already exists"); });

        User u = new User();
        u.setFullName(req.fullName);
        u.setEmail(req.email);

        return userRepository.save(u);
    }
}
