package com.example.ticketmanagement.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketDto {
    private UUID ticketId;
    private String subject;
    private String description;
    private String status;
    private String userId;
    private String assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
