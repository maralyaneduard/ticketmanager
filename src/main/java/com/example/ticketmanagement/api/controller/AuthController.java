package com.example.ticketmanagement.api.controller;

import com.example.ticketmanagement.api.dto.LoginRequestDto;
import com.example.ticketmanagement.api.dto.TokenResponse;
import com.example.ticketmanagement.api.exception.AuthException;
import com.example.ticketmanagement.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDto request) {
        try {
            var token = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
