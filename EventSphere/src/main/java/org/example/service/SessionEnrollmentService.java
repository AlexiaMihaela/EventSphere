package org.example.service;

import org.example.controller.dto.EnrollToSessionRequest;
import org.example.model.Session;
import org.example.model.SessionEnrollment;
import org.example.model.User;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionEnrollmentService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final EventRegistrationRepository registrationRepository;
    private final SessionEnrollmentRepository enrollmentRepository;

    public SessionEnrollmentService(SessionRepository sessionRepository,
                             UserRepository userRepository,
                             EventRegistrationRepository registrationRepository,
                             SessionEnrollmentRepository enrollmentRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public SessionEnrollment enroll(EnrollToSessionRequest req) {
        Session session = sessionRepository.findById(req.sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + req.sessionId));

        User user = userRepository.findById(req.userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId));

        Long eventId = session.getEvent().getId();

        // regula 1: user trebuie să fie registered la event
        boolean registered = registrationRepository.existsByEventIdAndUserId(eventId, user.getId());
        if (!registered) {
            throw new IllegalArgumentException("User must be registered to the event before enrolling in a session");
        }

        // regula 2: nu te înscrii de 2 ori la aceeași sesiune
        if (enrollmentRepository.existsBySessionIdAndUserId(session.getId(), user.getId())) {
            throw new IllegalArgumentException("User already enrolled in this session");
        }

        // regula 3: capacity
        long enrolledCount = enrollmentRepository.countBySessionId(session.getId());
        if (enrolledCount >= session.getCapacity()) {
            throw new IllegalArgumentException("Session is full");
        }

        SessionEnrollment enrollment = new SessionEnrollment();
        enrollment.setSession(session);
        enrollment.setUser(user);

        return enrollmentRepository.save(enrollment);
    }
}
