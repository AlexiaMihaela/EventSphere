package org.example.service;

import org.example.model.Event;
import org.example.repository.EventRepository;
import org.springframework.stereotype.Service;

import org.example.repository.EventRegistrationRepository;
import org.example.repository.FeedbackRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;


@Service
public class EventService {

    private final EventRepository eventRepository;

    private final SessionRepository sessionRepository;
    private final EventRegistrationRepository registrationRepository;
    private final SessionEnrollmentRepository enrollmentRepository;
    private final FeedbackRepository feedbackRepository;

    public EventService(EventRepository eventRepository,
                        SessionRepository sessionRepository,
                        EventRegistrationRepository registrationRepository,
                        SessionEnrollmentRepository enrollmentRepository,
                        FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.sessionRepository = sessionRepository;
        this.registrationRepository = registrationRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now());
    }

    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocationIgnoreCase(location);
    }

    @Transactional
    public void deleteEventCascade(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        var sessionIds = sessionRepository.findSessionIdsByEventId(eventId);

        if (!sessionIds.isEmpty()) {
            feedbackRepository.deleteBySessionIds(sessionIds);
            enrollmentRepository.deleteBySessionIds(sessionIds);
        }

        sessionRepository.deleteByEventId(eventId);
        registrationRepository.deleteByEventId(eventId);

        eventRepository.delete(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }

    public Event updateEvent(Long id, Event incoming) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));

        existing.setName(incoming.getName());
        existing.setLocation(incoming.getLocation());
        existing.setEventDate(incoming.getEventDate());
        existing.setMaxParticipants(incoming.getMaxParticipants());

        return eventRepository.save(existing);
    }

}
