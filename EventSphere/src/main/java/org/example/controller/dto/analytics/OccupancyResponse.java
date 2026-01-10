package org.example.controller.dto.analytics;

public class OccupancyResponse {
    public Long eventId;
    public long registrations;
    public int maxParticipants;
    public double occupancyRate; // 0..1
}
