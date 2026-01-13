package org.example.service;

import org.example.dto.CreateUserRequest;
import org.example.dto.UpdateUserRequest;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.FeedbackRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRegistrationRepository registrationRepository;
    private final SessionEnrollmentRepository enrollmentRepository;
    private final FeedbackRepository feedbackRepository;

    public UserService(UserRepository userRepository,
                       EventRegistrationRepository registrationRepository,
                       SessionEnrollmentRepository enrollmentRepository,
                       FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public User create(CreateUserRequest req) {
        userRepository.findByEmailIgnoreCase(req.email)
                .ifPresent(u -> { throw new IllegalArgumentException("Email already exists"); });

        User u = new User();
        u.setFullName(req.fullName);
        u.setEmail(req.email);

        return userRepository.save(u);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public User update(Long id, UpdateUserRequest req) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        userRepository.findByEmailIgnoreCase(req.email)
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new IllegalArgumentException("Email already exists");
                    }
                });

        existing.setFullName(req.fullName);
        existing.setEmail(req.email);

        return userRepository.save(existing);
    }

    @Transactional
    public void deleteCascade(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        feedbackRepository.deleteByUserId(userId);
        enrollmentRepository.deleteByUserId(userId);
        registrationRepository.deleteByUserId(userId);

        userRepository.delete(user);
    }

}
