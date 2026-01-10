package org.example.controller.dto.analytics;

import java.time.LocalDateTime;

public class SessionPerformanceRow {
    public Long sessionId;
    public String title;
    public LocalDateTime startTime;

    public long enrollments;
    public double avgRating;
    public long feedbackCount;

    public SessionPerformanceRow(Long sessionId, String title, LocalDateTime startTime,
                                 long enrollments, double avgRating, long feedbackCount) {
        this.sessionId = sessionId;
        this.title = title;
        this.startTime = startTime;
        this.enrollments = enrollments;
        this.avgRating = avgRating;
        this.feedbackCount = feedbackCount;
    }
}
