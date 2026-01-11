package org.example.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollToSessionRequest {

    @NotNull(message = "userId is required")
    public Long userId;

    @NotNull(message = "sessionId is required")
    public Long sessionId;
}
