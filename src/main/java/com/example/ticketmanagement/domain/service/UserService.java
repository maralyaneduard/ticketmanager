package com.example.ticketmanagement.domain.service;

import com.example.ticketmanagement.api.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findByUsername(String username);
    long count();
    List<UserDto> saveAll(List<UserDto> users);
}