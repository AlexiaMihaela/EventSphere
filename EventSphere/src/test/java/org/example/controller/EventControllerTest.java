package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Event;
import org.example.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEvent_shouldReturnEventJson() throws Exception {

        Event event = new Event();
        event.setName("JavaConf");
        event.setLocation("Bucharest");
        event.setEventDate(LocalDateTime.now().plusDays(5));
        event.setMaxParticipants(100);

        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("JavaConf"))
                .andExpect(jsonPath("$.location").value("Bucharest"))
                .andExpect(jsonPath("$.maxParticipants").value(100));
    }

    @Test
    void getEventById_whenMissing_shouldReturn400AndMessage() throws Exception {

        when(eventService.getEventById(99L))
                .thenThrow(new IllegalArgumentException("Event not found: 99"));

        mockMvc.perform(get("/events/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found: 99"));
    }

}
