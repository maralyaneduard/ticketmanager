package com.example.ticketmanagement.domain.service;

import com.example.ticketmanagement.api.dto.TicketDto;
import com.example.ticketmanagement.domain.model.Status;
import com.example.ticketmanagement.domain.model.TicketEntity;
import com.example.ticketmanagement.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public TicketDto create(final TicketDto ticketToSave, final String userId) {
        Assert.notNull(ticketToSave, "ticketToSave must not be null");
        Assert.hasText(userId, "userId must not be blank");
        Assert.hasText(ticketToSave.getSubject(), "subject must not be blank");

        var now = LocalDateTime.now();

        var ticketEntity = mapToNewEntity(ticketToSave, userId, now);
        var saved = ticketRepository.save(ticketEntity);

        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getUserTickets(final String userId, final boolean agent) {
        Assert.hasText(userId, "userId must not be blank");

        var tickets = agent
                ? ticketRepository.findByAssigneeId(userId)
                : ticketRepository.findByUserId(userId);

        return tickets.stream()
                .map(this::mapToDto)
                .toList();
    }

    private TicketEntity mapToNewEntity(TicketDto dto, String userId, LocalDateTime now) {
        TicketEntity e = new TicketEntity();
        e.setSubject(dto.getSubject().trim());
        e.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        e.setUserId(userId);
        e.setStatus(Status.OPEN);
        e.setAssigneeId(dto.getAssigneeId());
        e.setCreatedAt(now);
        e.setUpdatedAt(now);
        return e;
    }

    private TicketDto mapToDto(TicketEntity e) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(e.getTicketId());
        dto.setSubject(e.getSubject());
        dto.setDescription(e.getDescription());
        dto.setStatus(e.getStatus().name());
        dto.setUserId(e.getUserId());
        dto.setAssigneeId(e.getAssigneeId());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }
}
