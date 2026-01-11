package org.example.dto;

import java.time.LocalDateTime;

public class FeedbackResponse {
    public Long id;
    public Long sessionId;
    public Long userId;
    public Integer rating;
    public String comment;
    public LocalDateTime createdAt;

    public FeedbackResponse(Long id, Long sessionId, Long userId, Integer rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
