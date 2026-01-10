package org.example.controller;

import jakarta.validation.Valid;
import org.example.controller.dto.CreateSessionRequest;
import org.example.controller.dto.SessionResponse;
import org.example.model.Session;
import org.example.service.SessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SessionResponse create(@Valid @RequestBody CreateSessionRequest req) {
        return sessionService.create(req);
    }


    @GetMapping("/event/{eventId}")
    public List<SessionResponse> byEvent(@PathVariable Long eventId) {
        return sessionService.getByEvent(eventId);
    }

}
