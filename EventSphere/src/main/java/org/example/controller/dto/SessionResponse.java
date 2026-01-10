package org.example.controller.dto;

import java.time.LocalDateTime;

public class SessionResponse {
    public Long id;
    public String title;
    public LocalDateTime startTime;
    public Integer capacity;
    public Long eventId;

    public SessionResponse(Long id, String title, LocalDateTime startTime, Integer capacity, Long eventId) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.capacity = capacity;
        this.eventId = eventId;
    }
}
