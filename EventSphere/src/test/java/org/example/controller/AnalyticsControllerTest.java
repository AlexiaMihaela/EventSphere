package org.example.controller;

import org.example.dto.analytics.OccupancyResponse;
import org.example.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private AnalyticsService analyticsService;

    @Test
    void occupancy_shouldReturn200AndJson() throws Exception {
        OccupancyResponse resp = new OccupancyResponse();
        resp.eventId = 1L;
        resp.registrations = 20;
        resp.maxParticipants = 100;
        resp.occupancyRate = 0.2;

        when(analyticsService.getEventOccupancy(1L)).thenReturn(resp);

        mockMvc.perform(get("/analytics/events/{id}/occupancy", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.registrations").value(20))
                .andExpect(jsonPath("$.maxParticipants").value(100))
                .andExpect(jsonPath("$.occupancyRate").value(0.2));
    }

    @Test
    void occupancy_whenMissing_shouldReturn400() throws Exception {
        when(analyticsService.getEventOccupancy(99L))
                .thenThrow(new IllegalArgumentException("Event not found: 99"));

        mockMvc.perform(get("/analytics/events/{id}/occupancy", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found: 99"));
    }
}
