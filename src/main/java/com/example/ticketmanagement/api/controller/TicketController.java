package com.example.ticketmanagement.api.controller;

import com.example.ticketmanagement.api.dto.TicketDto;
import com.example.ticketmanagement.api.configuration.security.CurrentUser;
import com.example.ticketmanagement.domain.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private static final String AGENT_ROLE = "AGENT";
    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketDto> createTicket(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody TicketDto ticket
    ) {
        return ResponseEntity.ok(ticketService.create(ticket, currentUser.getUserId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'AGENT')")
    public ResponseEntity<List<TicketDto>> listTickets(@AuthenticationPrincipal CurrentUser currentUser) {
        boolean isAgent = AGENT_ROLE.equals(currentUser.getRole());

        return ResponseEntity.ok(ticketService.getUserTickets(currentUser.getUserId(), isAgent));
    }
}
