package org.example.controller;

import org.example.dto.analytics.OccupancyResponse;
import org.example.dto.analytics.SessionAttendanceRow;
import org.example.dto.analytics.SessionPerformanceRow;
import org.example.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/events/{eventId}/occupancy")
    public OccupancyResponse occupancy(@PathVariable Long eventId) {
        return analyticsService.getEventOccupancy(eventId);
    }

    @GetMapping("/events/{eventId}/sessions/low-attendance")
    public List<SessionAttendanceRow> lowAttendance(@PathVariable Long eventId,
                                                    @RequestParam(defaultValue = "5") long threshold) {
        return analyticsService.lowAttendance(eventId, threshold);
    }

    @GetMapping("/events/{eventId}/sessions/top")
    public List<SessionPerformanceRow> top(@PathVariable Long eventId,
                                           @RequestParam(defaultValue = "5") int limit) {
        return analyticsService.topSessions(eventId, limit);
    }
}
