package com.example.ticketmanagement.domain.repository;

import com.example.ticketmanagement.domain.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
    List<TicketEntity> findByUserId(String userId);

    List<TicketEntity> findByAssigneeId(String assigneeId);
}
