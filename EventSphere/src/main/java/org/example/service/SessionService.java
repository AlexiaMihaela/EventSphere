package org.example.service;

import org.example.controller.dto.CreateSessionRequest;
import org.example.controller.dto.SessionResponse;
import org.example.model.Event;
import org.example.model.Session;
import org.example.repository.EventRepository;
import org.example.repository.SessionRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
    }

    public SessionResponse create(CreateSessionRequest req) {
        Event event = eventRepository.findById(req.eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + req.eventId));

        Session s = new Session();
        s.setTitle(req.title);
        s.setStartTime(req.startTime);
        s.setCapacity(req.capacity);
        s.setEvent(event);

        Session saved = sessionRepository.save(s);

        SessionResponse resp = new SessionResponse();
        resp.id = saved.getId();
        resp.title = saved.getTitle();
        resp.startTime = saved.getStartTime();
        resp.capacity = saved.getCapacity();
        resp.eventId = event.getId();
        resp.eventName = event.getName();

        return resp;
    }


    public List<SessionResponse> getByEvent(Long eventId) {
        return sessionRepository.findSessionResponsesByEventId(eventId);
    }

}
