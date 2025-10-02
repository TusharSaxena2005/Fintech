package com.fintech.Backend.controllers;

import com.fintech.Backend.controllers.AuthController;
import com.fintech.Backend.dto.LoginDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.TransactionServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionServices transactionServices;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginDto loginDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password123");
        loginDto.setRole("USER");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFullName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setRole("USER");
        userDto.setAccountNumber("1234567890");
        userDto.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void login_ShouldReturnUserDto_WhenValidCredentials() throws Exception {
        // Arrange
        when(transactionServices.authenticate(any(LoginDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenInvalidCredentials() throws Exception {
        // Arrange
        when(transactionServices.authenticate(any(LoginDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenEmailIsEmpty() throws Exception {
        // Arrange
        loginDto.setEmail("");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }
}