package com.example.ticketmanagement.api.service;

public interface TokenService {

    String createToken(String userId, String role);
}
