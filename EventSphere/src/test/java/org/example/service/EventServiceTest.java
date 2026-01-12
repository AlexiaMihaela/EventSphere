package org.example.service;

import org.example.model.Event;
import org.example.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private EventRegistrationRepository registrationRepository;
    @Mock private SessionEnrollmentRepository enrollmentRepository;
    @Mock private FeedbackRepository feedbackRepository;

    @InjectMocks private EventService eventService;

    // 1) createEvent - cel mai simplu test (save + return)
    @Test
    void createEvent_shouldSaveAndReturn() {
        Event input = new Event();
        input.setName("JavaConf");

        Event saved = new Event();
        saved.setName("JavaConf");

        when(eventRepository.save(input)).thenReturn(saved);

        Event result = eventService.createEvent(input);

        assertSame(saved, result);
        verify(eventRepository).save(input);
        verifyNoMoreInteractions(eventRepository, sessionRepository, registrationRepository, enrollmentRepository, feedbackRepository);
    }

    // 2) getEventById - cazul important: not found
    @Test
    void getEventById_whenMissing_shouldThrow() {
        Long id = 99L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> eventService.getEventById(id));

        assertTrue(ex.getMessage().contains("Event not found"));
        verify(eventRepository).findById(id);
        verifyNoMoreInteractions(eventRepository, sessionRepository, registrationRepository, enrollmentRepository, feedbackRepository);
    }

    // 3) updateEvent - verifici că face mapping corect și dă save(existing)
    @Test
    void updateEvent_shouldCopyFieldsAndSave() {
        Long id = 1L;

        Event existing = new Event();
        existing.setName("Old");
        existing.setLocation("OldLoc");
        existing.setEventDate(LocalDateTime.now().plusDays(1));
        existing.setMaxParticipants(10);

        Event incoming = new Event();
        incoming.setName("NewName");
        incoming.setLocation("NewLoc");
        incoming.setEventDate(LocalDateTime.now().plusDays(10));
        incoming.setMaxParticipants(100);

        when(eventRepository.findById(id)).thenReturn(Optional.of(existing));
        when(eventRepository.save(existing)).thenAnswer(inv -> inv.getArgument(0)); // returnează exact obiectul salvat

        Event result = eventService.updateEvent(id, incoming);

        assertSame(existing, result);
        assertEquals("NewName", existing.getName());
        assertEquals("NewLoc", existing.getLocation());
        assertEquals(incoming.getEventDate(), existing.getEventDate());
        assertEquals(100, existing.getMaxParticipants());

        verify(eventRepository).findById(id);
        verify(eventRepository).save(existing);
        verifyNoMoreInteractions(eventRepository, sessionRepository, registrationRepository, enrollmentRepository, feedbackRepository);
    }


    @Test
    void deleteEventCascade_whenNoSessions_shouldSkipSessionChildrenDeletes() {
        Long eventId = 8L;

        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(sessionRepository.findSessionIdsByEventId(eventId)).thenReturn(List.of());

        eventService.deleteEventCascade(eventId);

        verify(feedbackRepository, never()).deleteBySessionIds(anyList());
        verify(enrollmentRepository, never()).deleteBySessionIds(anyList());

        verify(sessionRepository).deleteByEventId(eventId);
        verify(registrationRepository).deleteByEventId(eventId);
        verify(eventRepository).delete(event);
    }
}
