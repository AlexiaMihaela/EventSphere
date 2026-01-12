package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EnrollToSessionRequest;
import org.example.model.SessionEnrollment;
import org.example.service.SessionEnrollmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionEnrollmentController.class)
class SessionEnrollmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private SessionEnrollmentService enrollmentService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void enroll_shouldReturn200() throws Exception {
        EnrollToSessionRequest req = new EnrollToSessionRequest();
        req.sessionId = 10L;
        req.userId = 20L;

        when(enrollmentService.enroll(any(EnrollToSessionRequest.class)))
                .thenReturn(new SessionEnrollment());

        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void enroll_whenServiceThrows_shouldReturn400AndMessage() throws Exception {
        EnrollToSessionRequest req = new EnrollToSessionRequest();
        req.sessionId = 10L;
        req.userId = 20L;

        when(enrollmentService.enroll(any(EnrollToSessionRequest.class)))
                .thenThrow(new IllegalArgumentException("User already enrolled in this session"));

        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already enrolled in this session"));
    }
}
