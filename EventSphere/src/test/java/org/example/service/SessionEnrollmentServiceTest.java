package org.example.service;

import org.example.dto.EnrollToSessionRequest;
import org.example.model.Event;
import org.example.model.Session;
import org.example.model.SessionEnrollment;
import org.example.model.User;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionEnrollmentServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private UserRepository userRepository;
    @Mock private EventRegistrationRepository registrationRepository;
    @Mock private SessionEnrollmentRepository enrollmentRepository;

    @InjectMocks private SessionEnrollmentService enrollmentService;

    @Test
    void enroll_happyPath_shouldSaveEnrollment() {
        // given
        EnrollToSessionRequest req = new EnrollToSessionRequest();
        req.sessionId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(1L);

        Session session = mock(Session.class);
        when(session.getId()).thenReturn(10L);
        when(session.getCapacity()).thenReturn(100);
        when(session.getEvent()).thenReturn(event);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(1L, 20L)).thenReturn(true);
        when(enrollmentRepository.existsBySessionIdAndUserId(10L, 20L)).thenReturn(false);
        when(enrollmentRepository.countBySessionId(10L)).thenReturn(5L);

        SessionEnrollment saved = mock(SessionEnrollment.class);
        when(enrollmentRepository.save(any(SessionEnrollment.class))).thenReturn(saved);

        ArgumentCaptor<SessionEnrollment> captor = ArgumentCaptor.forClass(SessionEnrollment.class);

        // when
        SessionEnrollment result = enrollmentService.enroll(req);

        // then
        assertSame(saved, result);

        verify(enrollmentRepository).save(captor.capture());
        SessionEnrollment toSave = captor.getValue();
        assertSame(session, toSave.getSession());
        assertSame(user, toSave.getUser());

        verify(sessionRepository).findById(10L);
        verify(userRepository).findById(20L);
        verify(registrationRepository).existsByEventIdAndUserId(1L, 20L);
        verify(enrollmentRepository).existsBySessionIdAndUserId(10L, 20L);
        verify(enrollmentRepository).countBySessionId(10L);
        verifyNoMoreInteractions(sessionRepository, userRepository, registrationRepository, enrollmentRepository);
    }

    @Test
    void enroll_whenAlreadyEnrolled_shouldThrow() {
        // given
        EnrollToSessionRequest req = new EnrollToSessionRequest();
        req.sessionId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(1L);

        Session session = mock(Session.class);
        when(session.getId()).thenReturn(10L);
        when(session.getEvent()).thenReturn(event);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(1L, 20L)).thenReturn(true);
        when(enrollmentRepository.existsBySessionIdAndUserId(10L, 20L)).thenReturn(true);

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> enrollmentService.enroll(req));

        assertEquals("User already enrolled in this session", ex.getMessage());

        verify(enrollmentRepository, never()).save(any());
        verify(enrollmentRepository, never()).countBySessionId(anyLong());
    }

    @Test
    void enroll_whenSessionFull_shouldThrow() {
        // given
        EnrollToSessionRequest req = new EnrollToSessionRequest();
        req.sessionId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(1L);

        Session session = mock(Session.class);
        when(session.getId()).thenReturn(10L);
        when(session.getCapacity()).thenReturn(2);
        when(session.getEvent()).thenReturn(event);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(1L, 20L)).thenReturn(true);
        when(enrollmentRepository.existsBySessionIdAndUserId(10L, 20L)).thenReturn(false);
        when(enrollmentRepository.countBySessionId(10L)).thenReturn(2L); // full

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> enrollmentService.enroll(req));

        assertEquals("Session is full", ex.getMessage());

        verify(enrollmentRepository, never()).save(any());
    }
}
