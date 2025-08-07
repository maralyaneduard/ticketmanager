package com.example.ticketmanagement.api.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUser {
    private final String userId;
    private final String role;
}
