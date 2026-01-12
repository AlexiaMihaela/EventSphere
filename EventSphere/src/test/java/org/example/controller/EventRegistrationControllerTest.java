package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.RegisterToEventRequest;
import org.example.model.EventRegistration;
import org.example.service.EventRegistrationService;
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

@WebMvcTest(EventRegistrationController.class)
class EventRegistrationControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private EventRegistrationService registrationService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void register_shouldReturn200() throws Exception {
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 10L;
        req.userId = 20L;

        // Entity returnată: nu ne trebuie câmpuri, doar să fie JSON ok.
        EventRegistration resp = new EventRegistration();
        when(registrationService.register(any(RegisterToEventRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void register_whenServiceThrows_shouldReturn400AndMessage() throws Exception {
        RegisterToEventRequest req = new RegisterToEventRequest();
        req.eventId = 10L;
        req.userId = 20L;

        when(registrationService.register(any(RegisterToEventRequest.class)))
                .thenThrow(new IllegalArgumentException("Event is full"));

        mockMvc.perform(post("/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event is full"));
    }
}
