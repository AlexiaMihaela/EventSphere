package org.example.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddFeedbackRequest {

    @NotNull(message = "userId is required")
    public Long userId;

    @NotNull(message = "sessionId is required")
    public Long sessionId;

    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    public Integer rating;

    @NotBlank(message = "comment is required")
    public String comment;
}
