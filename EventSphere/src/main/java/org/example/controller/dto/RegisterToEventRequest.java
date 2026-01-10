package org.example.controller.dto;

import jakarta.validation.constraints.NotNull;

public class RegisterToEventRequest {

    @NotNull(message = "userId is required")
    public Long userId;

    @NotNull(message = "eventId is required")
    public Long eventId;
}
