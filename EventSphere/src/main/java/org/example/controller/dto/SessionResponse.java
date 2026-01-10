package org.example.controller.dto;

import java.time.LocalDateTime;

public class SessionResponse {
    public Long id;
    public String title;
    public LocalDateTime startTime;
    public Integer capacity;

    public Long eventId;
    public String eventName;
}
