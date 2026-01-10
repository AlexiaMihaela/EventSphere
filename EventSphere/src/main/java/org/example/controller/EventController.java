package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Event;
import org.example.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event createEvent(@Valid @RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/location/{location}")
    public List<Event> getEventsByLocation(@PathVariable String location) {
        return eventService.getEventsByLocation(location);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEventCascade(id);
    }

}
