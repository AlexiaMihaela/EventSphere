package org.example.service;

import org.example.dto.RegisterToEventRequest;
import org.example.model.Event;
import org.example.model.EventRegistration;
import org.example.model.User;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.EventRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventRegistrationService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventRegistrationRepository registrationRepository;

    public EventRegistrationService(EventRepository eventRepository,
                               UserRepository userRepository,
                               EventRegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public EventRegistration register(RegisterToEventRequest req) {
        Event event = eventRepository.findById(req.eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + req.eventId));

        User user = userRepository.findById(req.userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId));

        if (registrationRepository.existsByEventIdAndUserId(event.getId(), user.getId())) {
            throw new IllegalArgumentException("User already registered to this event");
        }

        long registered = registrationRepository.countByEventId(event.getId());
        if (registered >= event.getMaxParticipants()) {
            throw new IllegalArgumentException("Event is full");
        }

        EventRegistration reg = new EventRegistration();
        reg.setEvent(event);
        reg.setUser(user);

        return registrationRepository.save(reg);
    }
}
