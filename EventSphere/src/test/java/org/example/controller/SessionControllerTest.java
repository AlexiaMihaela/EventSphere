package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CreateSessionRequest;
import org.example.dto.SessionResponse;
import org.example.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private SessionService sessionService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void getById_whenExists_shouldReturn200() throws Exception {
        SessionResponse resp = new SessionResponse(1L, "Intro", LocalDateTime.now().plusDays(2), 20, 10L);
        when(sessionService.getById(1L)).thenReturn(resp);

        mockMvc.perform(get("/sessions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Intro"))
                .andExpect(jsonPath("$.capacity").value(20))
                .andExpect(jsonPath("$.eventId").value(10));
    }

    @Test
    void getById_whenMissing_shouldReturn400AndMessage() throws Exception {
        when(sessionService.getById(99L))
                .thenThrow(new IllegalArgumentException("Session not found: 99"));

        mockMvc.perform(get("/sessions/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Session not found: 99"));
    }

    @Test
    void create_shouldReturnSessionResponse() throws Exception {
        CreateSessionRequest req = new CreateSessionRequest();
        req.eventId = 10L;
        req.title = "Testing";
        req.startTime = LocalDateTime.now().plusDays(5);
        req.capacity = 40;

        SessionResponse resp = new SessionResponse(5L, "Testing", req.startTime, 40, 10L);
        when(sessionService.create(any(CreateSessionRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Testing"))
                .andExpect(jsonPath("$.capacity").value(40))
                .andExpect(jsonPath("$.eventId").value(10));
    }
}
