package com.example.ticketmanagement.api.configuration;

import com.example.ticketmanagement.api.dto.UserDto;
import com.example.ticketmanagement.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitialUserDataLoader implements ApplicationRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        var agentUUID = UUID.randomUUID();
        System.out.println("!!!!!!!!! FOR TEST !!!!!!!!AGENT ID IS " + agentUUID);
        if (userService.count() == 0) {
            userService.saveAll(List.of(
                new UserDto(UUID.randomUUID(), "john",   passwordEncoder.encode("pass123"),  "USER"),
                new UserDto(agentUUID, "agent1", passwordEncoder.encode("agentpass"), "AGENT")
            ));
        }
    }
}