package org.example.service;

import org.example.dto.CreateSessionRequest;
import org.example.dto.SessionResponse;
import org.example.model.Event;
import org.example.model.Session;
import org.example.repository.EventRepository;
import org.example.repository.FeedbackRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private EventRepository eventRepository;
    @Mock private SessionEnrollmentRepository enrollmentRepository;
    @Mock private FeedbackRepository feedbackRepository;

    @InjectMocks private SessionService sessionService;

    @Test
    void create_whenEventExists_shouldSaveAndReturnResponse() {
        // given
        Long eventId = 1L;

        // Event fără setId(): simulăm doar getId()
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(eventId);

        CreateSessionRequest req = new CreateSessionRequest();
        req.eventId = eventId;
        req.title = "Intro Spring";
        req.startTime = LocalDateTime.now().plusDays(3);
        req.capacity = 30;

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Capturăm ce se trimite la save()
        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);

        // Session salvată: iarăși fără setId(), simulăm getId()
        Session saved = mock(Session.class);
        when(saved.getId()).thenReturn(10L);
        when(saved.getTitle()).thenReturn(req.title);
        when(saved.getStartTime()).thenReturn(req.startTime);
        when(saved.getCapacity()).thenReturn(req.capacity);

        when(sessionRepository.save(any(Session.class))).thenReturn(saved);

        // when
        SessionResponse resp = sessionService.create(req);

        // then: response
        assertEquals(10L, resp.id);
        assertEquals("Intro Spring", resp.title);
        assertEquals(req.startTime, resp.startTime);
        assertEquals(30, resp.capacity);
        assertEquals(1L, resp.eventId);

        // then: ce s-a salvat
        verify(sessionRepository).save(captor.capture());
        Session toSave = captor.getValue();

        assertEquals("Intro Spring", toSave.getTitle());
        assertEquals(req.startTime, toSave.getStartTime());
        assertEquals(30, toSave.getCapacity());
        assertSame(event, toSave.getEvent());

        verify(eventRepository).findById(eventId);
        verifyNoMoreInteractions(eventRepository, sessionRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void create_whenEventMissing_shouldThrow() {
        // given
        CreateSessionRequest req = new CreateSessionRequest();
        req.eventId = 99L;
        req.title = "X";
        req.startTime = LocalDateTime.now().plusDays(1);
        req.capacity = 10;

        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.create(req));

        assertTrue(ex.getMessage().contains("Event not found: 99"));

        verify(eventRepository).findById(99L);
        verifyNoInteractions(sessionRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void getById_whenMissing_shouldThrow() {
        // given
        Long sessionId = 77L;
        when(sessionRepository.findSessionResponseById(sessionId)).thenReturn(null);

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.getById(sessionId));

        assertTrue(ex.getMessage().contains("Session not found: 77"));

        verify(sessionRepository).findSessionResponseById(sessionId);
        verifyNoMoreInteractions(sessionRepository);
        verifyNoInteractions(eventRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void deleteCascade_shouldDeleteChildrenThenSession() {
        // given
        Long sessionId = 5L;

        Session existing = mock(Session.class);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(existing));

        // when
        sessionService.deleteCascade(sessionId);

        // then: ordine corectă
        InOrder inOrder = inOrder(sessionRepository, feedbackRepository, enrollmentRepository);

        inOrder.verify(sessionRepository).findById(sessionId);
        inOrder.verify(feedbackRepository).deleteBySessionId(sessionId);
        inOrder.verify(enrollmentRepository).deleteBySessionId(sessionId);
        inOrder.verify(sessionRepository).delete(existing);

        verifyNoMoreInteractions(sessionRepository, feedbackRepository, enrollmentRepository);
        verifyNoInteractions(eventRepository);
    }
}
