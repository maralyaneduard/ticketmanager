package com.example.ticketmanagement.domain.service;

import com.example.ticketmanagement.api.dto.UserDto;
import com.example.ticketmanagement.domain.model.UserEntity;
import com.example.ticketmanagement.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByUsername(final String username) {
        Assert.hasText(username, "username must not be blank");

        return userRepository.findByUsername(username)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public List<UserDto> saveAll(final List<UserDto> userDtos) {
        Assert.notEmpty(userDtos, "userDtos must not be empty");

        List<UserEntity> toSave = userDtos.stream()
                .map(this::toEntityForUpsert)
                .toList();

        List<UserEntity> saved = userRepository.saveAll(toSave);

        return saved.stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(UserEntity e) {
        UserDto dto = new UserDto();
        dto.setUserId(e.getUserId());
        dto.setUsername(e.getUsername());
        dto.setPassword(e.getPassword());
        dto.setRole(e.getRole());
        return dto;
    }

    private UserEntity toEntityForUpsert(UserDto dto) {
        Assert.notNull(dto, "user dto must not be null");
        Assert.hasText(dto.getUsername(), "username must not be blank");
        Assert.hasText(dto.getRole(), "role must not be blank");

        UserEntity e = new UserEntity();
        e.setUserId(dto.getUserId());
        e.setUsername(dto.getUsername().trim());
        e.setPassword(dto.getPassword());
        e.setRole(dto.getRole().trim());
        return e;
    }
}
