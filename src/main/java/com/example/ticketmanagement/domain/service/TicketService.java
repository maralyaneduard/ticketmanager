package com.example.ticketmanagement.domain.service;

import com.example.ticketmanagement.api.dto.TicketDto;

import java.util.List;

public interface TicketService {

    TicketDto create(TicketDto ticketToSave, String userId);
    List<TicketDto> getUserTickets(String userId, boolean agent);
}
