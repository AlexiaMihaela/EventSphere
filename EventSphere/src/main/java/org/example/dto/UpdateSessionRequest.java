package org.example.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateSessionRequest {

    @NotBlank(message = "title is required")
    public String title;

    @NotNull(message = "startTime is required")
    @Future(message = "startTime must be in the future")
    public LocalDateTime startTime;

    @NotNull(message = "capacity is required")
    @Min(value = 1, message = "capacity must be at least 1")
    public Integer capacity;

}
