package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.EnrollToSessionRequest;
import org.example.model.SessionEnrollment;
import org.example.service.SessionEnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
public class SessionEnrollmentController {

    private final SessionEnrollmentService enrollmentService;

    public SessionEnrollmentController(SessionEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public SessionEnrollment enroll(@Valid @RequestBody EnrollToSessionRequest req) {
        return enrollmentService.enroll(req);
    }
}
