package com.example.ticketmanagement.api.service;

import com.example.ticketmanagement.api.exception.AuthException;
import com.example.ticketmanagement.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public String login(final String username, final String rawPassword) {
        var user = userService.findByUsername(username).orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }

        return tokenService.createToken(user.getUserId().toString(), user.getRole());
    }
}