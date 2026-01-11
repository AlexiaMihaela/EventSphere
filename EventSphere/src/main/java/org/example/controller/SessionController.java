package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CreateSessionRequest;
import org.example.dto.SessionResponse;
import org.example.dto.UpdateSessionRequest;
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

    @GetMapping
    public List<SessionResponse> getAll() {
        return sessionService.getAll();
    }

    @GetMapping("/{id}")
    public SessionResponse getById(@PathVariable Long id) {
        return sessionService.getById(id);
    }

    @PutMapping("/{id}")
    public SessionResponse update(@PathVariable Long id, @Valid @RequestBody UpdateSessionRequest req) {
        return sessionService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sessionService.deleteCascade(id);
    }
}
