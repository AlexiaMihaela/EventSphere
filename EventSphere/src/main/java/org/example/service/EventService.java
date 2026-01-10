package org.example.service;

import org.example.model.Event;
import org.example.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}
