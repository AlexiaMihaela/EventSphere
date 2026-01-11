package org.example.service;


import org.example.dto.CreateSessionRequest;
import org.example.dto.SessionResponse;
import org.example.dto.UpdateSessionRequest;
import org.example.model.Event;
import org.example.model.Session;
import org.example.repository.EventRepository;
import org.example.repository.FeedbackRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final SessionEnrollmentRepository enrollmentRepository;
    private final FeedbackRepository feedbackRepository;

    public SessionService(SessionRepository sessionRepository,
                          EventRepository eventRepository,
                          SessionEnrollmentRepository enrollmentRepository,
                          FeedbackRepository feedbackRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.feedbackRepository = feedbackRepository;
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

        return resp;
    }


    public List<SessionResponse> getByEvent(Long eventId) {
        return sessionRepository.findSessionResponsesByEventId(eventId);
    }

    public List<SessionResponse> getAll() {
        return sessionRepository.findAllSessionResponses();
    }

    public SessionResponse getById(Long sessionId) {
        SessionResponse resp = sessionRepository.findSessionResponseById(sessionId);
        if (resp == null) throw new IllegalArgumentException("Session not found: " + sessionId);
        return resp;
    }

    @Transactional
    public SessionResponse update(Long sessionId, UpdateSessionRequest req) {
        Session existing = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        existing.setTitle(req.title);
        existing.setStartTime(req.startTime);
        existing.setCapacity(req.capacity);

        Session saved = sessionRepository.save(existing);
        return new SessionResponse(saved.getId(), saved.getTitle(), saved.getStartTime(), saved.getCapacity(), saved.getEvent().getId());
    }

    @Transactional
    public void deleteCascade(Long sessionId) {
        Session existing = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        // delete children first
        feedbackRepository.deleteBySessionId(sessionId);
        enrollmentRepository.deleteBySessionId(sessionId);

        sessionRepository.delete(existing);
    }

}
