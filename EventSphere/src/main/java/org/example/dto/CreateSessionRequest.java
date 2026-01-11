package org.example.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateSessionRequest {

    @NotBlank
    public String title;

    @NotNull
    @Future
    public LocalDateTime startTime;

    @NotNull
    @Min(1)
    public Integer capacity;

    @NotNull
    public Long eventId;
}
