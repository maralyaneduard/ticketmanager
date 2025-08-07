package com.example.ticketmanagement.controller;

import com.example.ticketmanagement.api.controller.AuthController;
import com.example.ticketmanagement.api.dto.LoginRequestDto;
import com.example.ticketmanagement.api.dto.TokenResponse;
import com.example.ticketmanagement.api.exception.AuthException;
import com.example.ticketmanagement.api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_shouldReturnToken_givenAuthSuccessful() {
        var request = new LoginRequestDto("john", "pass123");
        var token = "mocked-token";

        when(authService.login("john", "pass123")).thenReturn(token);

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo(token);

        verify(authService).login("john", "pass123");
    }

    @Test
    void login_shouldReturn401_givenAuthFails() {
        var request = new LoginRequestDto("john", "wrongpass");

        when(authService.login("john", "wrongpass"))
                .thenThrow(new AuthException("Invalid credentials"));

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService).login("john", "wrongpass");
    }

    @Test
    void login_shouldReturn500_givenUnexpectedExceptionOccurs() {
        var request = new LoginRequestDto("john", "pass123");

        when(authService.login("john", "pass123"))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        verify(authService).login("john", "pass123");
    }
}