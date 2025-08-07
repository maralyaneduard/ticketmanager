package com.example.ticketmanagement.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.ticketmanagement.api.dto.UserDto;
import com.example.ticketmanagement.domain.model.UserEntity;
import com.example.ticketmanagement.domain.repository.UserRepository;
import com.example.ticketmanagement.domain.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity sampleEntity;
    private UserDto sampleDto;

    @BeforeEach
    void setup() {
        sampleEntity = new UserEntity();
        sampleEntity.setUserId(UUID.randomUUID());
        sampleEntity.setUsername("john");
        sampleEntity.setPassword("pass123");
        sampleEntity.setRole("USER");

        sampleDto = new UserDto();
        sampleDto.setUserId(sampleEntity.getUserId());
        sampleDto.setUsername("john");
        sampleDto.setPassword("pass123");
        sampleDto.setRole("USER");
    }

    @Test
    void findByUsername_shouldReturnUserDto_GivenUserExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(sampleEntity));

        var result = userService.findByUsername("john");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john");
        verify(userRepository).findByUsername("john");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByUsername_shouldThrow_givenInvalidUsername(String input) {
        assertThatThrownBy(() -> userService.findByUsername(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username must not be blank");

        verifyNoInteractions(userRepository);
    }

    @Test
    void count_shouldReturnCount() {
        long randomLong = (long) RandomUtil.getPositiveInt();
        when(userRepository.count()).thenReturn(randomLong);

        var count = userService.count();

        assertThat(count).isEqualTo(randomLong);
        verify(userRepository).count();
    }

    @Test
    void saveAll_shouldCreateUsers_givenUserData() {
        when(userRepository.saveAll(anyList())).thenReturn(List.of(sampleEntity));

        List<UserDto> result = userService.saveAll(List.of(sampleDto));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("john");
        verify(userRepository).saveAll(anyList());
    }

    @ParameterizedTest
    @MethodSource("invalidSaveInputs")
    void saveAll_shouldThrow_givenInvalidInputs(List<UserDto> input, String expectedMessage) {
        assertThatThrownBy(() -> userService.saveAll(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);

        verifyNoInteractions(userRepository);
    }

    static Stream<Arguments> invalidSaveInputs() {
        return Stream.of(
                Arguments.of(null, "userDtos must not be empty"),
                Arguments.of(List.of(), "userDtos must not be empty"),
                Arguments.of(List.of(new UserDto()), "username must not be blank"),
                Arguments.of(List.of(userDtoWithUsername("john", null)), "role must not be blank"),
                Arguments.of(List.of(userDtoWithUsername("  ", "USER")), "username must not be blank"),
                Arguments.of(List.of(userDtoWithUsername("john", "  ")), "role must not be blank")
        );
    }

    private static UserDto userDtoWithUsername(String username, String role) {
        var dto = new UserDto();
        dto.setUsername(username);
        dto.setRole(role);
        dto.setPassword("pass123");
        dto.setUserId(UUID.randomUUID());
        return dto;
    }
}
