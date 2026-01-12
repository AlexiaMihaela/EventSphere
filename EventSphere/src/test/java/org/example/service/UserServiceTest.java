package org.example.service;

import org.example.dto.CreateUserRequest;
import org.example.dto.UpdateUserRequest;
import org.example.model.User;
import org.example.repository.EventRegistrationRepository;
import org.example.repository.FeedbackRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private EventRegistrationRepository registrationRepository;
    @Mock private SessionEnrollmentRepository enrollmentRepository;
    @Mock private FeedbackRepository feedbackRepository;

    @InjectMocks private UserService userService;

    @Test
    void create_whenEmailFree_shouldSaveUser() {
        // given
        CreateUserRequest req = new CreateUserRequest();
        req.fullName = "Ana Pop";
        req.email = "ana@test.com";

        when(userRepository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.empty());

        // user salvat (fără setId, nu contează aici)
        User saved = mock(User.class);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // when
        User result = userService.create(req);

        // then
        assertSame(saved, result);

        verify(userRepository).findByEmailIgnoreCase("ana@test.com");
        verify(userRepository).save(captor.capture());

        User toSave = captor.getValue();
        assertEquals("Ana Pop", toSave.getFullName());
        assertEquals("ana@test.com", toSave.getEmail());

        verifyNoMoreInteractions(userRepository, registrationRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void create_whenEmailExists_shouldThrow() {
        // given
        CreateUserRequest req = new CreateUserRequest();
        req.fullName = "Ana Pop";
        req.email = "ana@test.com";

        User existing = mock(User.class);
        when(userRepository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.of(existing));

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.create(req));

        assertEquals("Email already exists", ex.getMessage());

        verify(userRepository).findByEmailIgnoreCase("ana@test.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(registrationRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void update_whenEmailTakenByOtherUser_shouldThrow() {
        // given
        Long id = 1L;

        UpdateUserRequest req = new UpdateUserRequest();
        req.fullName = "New Name";
        req.email = "taken@test.com";

        User existing = mock(User.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));

        // user cu același email, dar alt id => trebuie să arunce
        User other = mock(User.class);
        when(other.getId()).thenReturn(2L);
        when(userRepository.findByEmailIgnoreCase("taken@test.com")).thenReturn(Optional.of(other));

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.update(id, req));

        assertEquals("Email already exists", ex.getMessage());

        verify(userRepository).findById(id);
        verify(userRepository).findByEmailIgnoreCase("taken@test.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(registrationRepository, enrollmentRepository, feedbackRepository);
    }

    @Test
    void deleteCascade_shouldDeleteChildrenThenUser() {
        // given
        Long userId = 5L;

        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        userService.deleteCascade(userId);

        // then: ordine corectă
        InOrder inOrder = inOrder(userRepository, feedbackRepository, enrollmentRepository, registrationRepository);

        inOrder.verify(userRepository).findById(userId);
        inOrder.verify(feedbackRepository).deleteByUserId(userId);
        inOrder.verify(enrollmentRepository).deleteByUserId(userId);
        inOrder.verify(registrationRepository).deleteByUserId(userId);
        inOrder.verify(userRepository).delete(user);

        verifyNoMoreInteractions(userRepository, feedbackRepository, enrollmentRepository, registrationRepository);
    }
}
