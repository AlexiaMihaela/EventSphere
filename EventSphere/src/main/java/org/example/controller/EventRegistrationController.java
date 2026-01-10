package org.example.controller;

import jakarta.validation.Valid;
import org.example.controller.dto.RegisterToEventRequest;
import org.example.model.EventRegistration;
import org.example.service.EventRegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrations")
public class EventRegistrationController {

    private final EventRegistrationService registrationService;

    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public EventRegistration register(@Valid @RequestBody RegisterToEventRequest req) {
        return registrationService.register(req);
    }
}
