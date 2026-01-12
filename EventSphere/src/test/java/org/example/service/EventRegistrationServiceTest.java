package org.example.service;

import org.example.dto.RegisterToEventRequest;
import org.example.model.Event;
import org.example.model.EventRegistration;
import org.example.model.User;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.EventRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRegistrationServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private UserRepository userRepository;
    @Mock private EventRegistrationRepository registrationRepository;

    @InjectMocks private EventRegistrationService registrationService;

    @Test
    void register_happyPath_shouldSaveRegistration() {
        // given
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(10L);
        when(event.getMaxParticipants()).thenReturn(100);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(10L, 20L)).thenReturn(false);
        when(registrationRepository.countByEventId(10L)).thenReturn(5L);

        EventRegistration saved = mock(EventRegistration.class);
        when(registrationRepository.save(any(EventRegistration.class))).thenReturn(saved);

        ArgumentCaptor<EventRegistration> captor = ArgumentCaptor.forClass(EventRegistration.class);

        // when
        EventRegistration result = registrationService.register(req);

        // then
        assertSame(saved, result);

        verify(registrationRepository).save(captor.capture());
        EventRegistration toSave = captor.getValue();
        assertSame(event, toSave.getEvent());
        assertSame(user, toSave.getUser());

        verify(eventRepository).findById(10L);
        verify(userRepository).findById(20L);
        verify(registrationRepository).existsByEventIdAndUserId(10L, 20L);
        verify(registrationRepository).countByEventId(10L);
        verifyNoMoreInteractions(eventRepository, userRepository, registrationRepository);
    }

    @Test
    void register_whenEventMissing_shouldThrow() {
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 99L;
        req.userId = 20L;

        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.register(req));

        assertTrue(ex.getMessage().contains("Event not found: 99"));

        verify(eventRepository).findById(99L);
        verifyNoInteractions(userRepository, registrationRepository);
    }

    @Test
    void register_whenAlreadyRegistered_shouldThrow() {
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(10L);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(10L, 20L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.register(req));

        assertEquals("User already registered to this event", ex.getMessage());

        verify(registrationRepository).existsByEventIdAndUserId(10L, 20L);
        verify(registrationRepository, never()).save(any());
        verify(registrationRepository, never()).countByEventId(anyLong());
    }

    @Test
    void register_whenEventFull_shouldThrow() {
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 10L;
        req.userId = 20L;

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(10L);
        when(event.getMaxParticipants()).thenReturn(2);

        User user = mock(User.class);
        when(user.getId()).thenReturn(20L);

        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        when(registrationRepository.existsByEventIdAndUserId(10L, 20L)).thenReturn(false);
        when(registrationRepository.countByEventId(10L)).thenReturn(2L); // full

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.register(req));

        assertEquals("Event is full", ex.getMessage());

        verify(registrationRepository, never()).save(any());
    }
}
