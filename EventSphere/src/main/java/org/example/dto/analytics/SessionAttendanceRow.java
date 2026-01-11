package org.example.dto.analytics;

import java.time.LocalDateTime;

public class SessionAttendanceRow {
    public Long sessionId;
    public String title;
    public LocalDateTime startTime;
    public long enrollments;

    public SessionAttendanceRow(Long sessionId, String title, LocalDateTime startTime, long enrollments) {
        this.sessionId = sessionId;
        this.title = title;
        this.startTime = startTime;
        this.enrollments = enrollments;
    }
}
