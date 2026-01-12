package org.example.service;

import org.example.dto.analytics.OccupancyResponse;
import org.example.dto.analytics.SessionPerformanceRow;
import org.example.model.Event;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.EventRepository;
import org.example.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private EventRegistrationRepository registrationRepository;
    @Mock private SessionRepository sessionRepository;

    @InjectMocks private AnalyticsService analyticsService;

    @Test
    void getEventOccupancy_shouldCalculateCorrectly() {
        // given
        Long eventId = 1L;

        Event event = mock(Event.class);
        when(event.getMaxParticipants()).thenReturn(100);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(registrationRepository.countByEventId(eventId)).thenReturn(25L);

        // when
        OccupancyResponse resp = analyticsService.getEventOccupancy(eventId);

        // then
        assertEquals(1L, resp.eventId);
        assertEquals(25L, resp.registrations);
        assertEquals(100, resp.maxParticipants);
        assertEquals(0.25, resp.occupancyRate);

        verify(eventRepository).findById(eventId);
        verify(registrationRepository).countByEventId(eventId);
        verifyNoMoreInteractions(eventRepository, registrationRepository, sessionRepository);
    }

    @Test
    void getEventOccupancy_whenEventMissing_shouldThrow() {
        Long eventId = 99L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> analyticsService.getEventOccupancy(eventId));

        assertTrue(ex.getMessage().contains("Event not found: 99"));

        verify(eventRepository).findById(eventId);
        verifyNoInteractions(registrationRepository, sessionRepository);
    }

    @Test
    void topSessions_shouldRespectLimit() {
        // given
        Long eventId = 1L;

        SessionPerformanceRow r1 = mock(SessionPerformanceRow.class);
        SessionPerformanceRow r2 = mock(SessionPerformanceRow.class);
        SessionPerformanceRow r3 = mock(SessionPerformanceRow.class);

        when(sessionRepository.findTopSessionPerformance(eventId))
                .thenReturn(List.of(r1, r2, r3));

        // when
        List<SessionPerformanceRow> result = analyticsService.topSessions(eventId, 2);

        // then
        assertEquals(2, result.size());
        assertSame(r1, result.get(0));
        assertSame(r2, result.get(1));

        verify(sessionRepository).findTopSessionPerformance(eventId);
        verifyNoMoreInteractions(sessionRepository, eventRepository, registrationRepository);
    }
}
