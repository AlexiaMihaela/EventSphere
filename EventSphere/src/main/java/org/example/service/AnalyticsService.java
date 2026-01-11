package org.example.service;

import org.example.dto.analytics.OccupancyResponse;
import org.example.dto.analytics.SessionAttendanceRow;
import org.example.dto.analytics.SessionPerformanceRow;
import org.example.model.Event;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.EventRepository;
import org.example.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final SessionRepository sessionRepository;

    public AnalyticsService(EventRepository eventRepository,
                            EventRegistrationRepository registrationRepository,
                            SessionRepository sessionRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.sessionRepository = sessionRepository;
    }

    public OccupancyResponse getEventOccupancy(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        long regs = registrationRepository.countByEventId(eventId);
        int max = event.getMaxParticipants();

        OccupancyResponse resp = new OccupancyResponse();
        resp.eventId = eventId;
        resp.registrations = regs;
        resp.maxParticipants = max;
        resp.occupancyRate = (max == 0) ? 0.0 : ((double) regs / (double) max);

        return resp;
    }

    public List<SessionAttendanceRow> lowAttendance(Long eventId, long threshold) {
        return sessionRepository.findLowAttendanceSessions(eventId, threshold);
    }

    public List<SessionPerformanceRow> topSessions(Long eventId, int limit) {
        return sessionRepository.findTopSessionPerformance(eventId)
                .stream()
                .limit(limit)
                .toList();
    }
}
