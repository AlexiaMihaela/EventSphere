package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CreateUserRequest;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void create_shouldReturnUserJson() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.fullName = "Ana Pop";
        req.email = "ana@test.com";

        // nu avem setId => folosim mock și simulăm getter-ele
        User resp = mock(User.class);
        when(resp.getId()).thenReturn(1L);
        when(resp.getFullName()).thenReturn("Ana Pop");
        when(resp.getEmail()).thenReturn("ana@test.com");

        when(userService.create(any(CreateUserRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Ana Pop"))
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }

    @Test
    void getById_whenExists_shouldReturn200() throws Exception {
        User resp = mock(User.class);
        when(resp.getId()).thenReturn(2L);
        when(resp.getFullName()).thenReturn("Ion Ionescu");
        when(resp.getEmail()).thenReturn("ion@test.com");

        when(userService.getById(2L)).thenReturn(resp);

        mockMvc.perform(get("/users/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.fullName").value("Ion Ionescu"))
                .andExpect(jsonPath("$.email").value("ion@test.com"));
    }

    @Test
    void getById_whenMissing_shouldReturn400AndMessage() throws Exception {
        when(userService.getById(99L))
                .thenThrow(new IllegalArgumentException("User not found: 99"));

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found: 99"));
    }
}
