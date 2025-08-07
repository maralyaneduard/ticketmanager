package com.example.ticketmanagement.api.service;

import com.example.ticketmanagement.api.configuration.security.JweUtils;
import com.example.ticketmanagement.api.exception.CouldNotCreateTokenException;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException; // Adjust based on actual JWE lib you're using

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String createToken(final String userId, final String role) {
        try {
            return JweUtils.generateToken(userId, role);
        } catch (IllegalArgumentException | JOSEException e) {
            throw new CouldNotCreateTokenException("Failed to generate token", e.getCause());
        }
    }
}
