package com.example.ticketmanagement.service;

import com.example.ticketmanagement.api.dto.TicketDto;
import com.example.ticketmanagement.domain.model.Status;
import com.example.ticketmanagement.domain.model.TicketEntity;
import com.example.ticketmanagement.domain.repository.TicketRepository;
import com.example.ticketmanagement.domain.service.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void create_ShouldReturnSavedTicketDto_givenTicketIsSaved() {
        var userId = "user-1";
        var ticketDto = buildTicketDto();
        var entity = buildTicketEntity(userId, ticketDto);

        when(ticketRepository.save(any())).thenReturn(entity);

        var result = ticketService.create(ticketDto, userId);

        assertThat(result).isNotNull();
        assertThat(result.getTicketId()).isEqualTo(entity.getTicketId());
        verify(ticketRepository, times(1)).save(any());
    }

    @ParameterizedTest
    @MethodSource("invalidCreateInputs")
    void create_ShouldThrow_givenInvalidInputs(TicketDto dto, String userId, String expectedMsg) {
        assertThatThrownBy(() -> ticketService.create(dto, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMsg);
        verifyNoInteractions(ticketRepository);
    }

    static Stream<Arguments> invalidCreateInputs() {
        TicketDto nullDto = null;
        var blankSubjectTicket = new TicketDto();
        blankSubjectTicket.setSubject("   ");
        var nullSubjectTicket = new TicketDto();

        return Stream.of(
                Arguments.of(nullDto, "user", "ticketToSave must not be null"),
                Arguments.of(blankSubjectTicket, "user", "subject must not be blank"),
                Arguments.of(nullSubjectTicket, "user", "subject must not be blank"),
                Arguments.of(new TicketDto(), null, "userId must not be blank"),
                Arguments.of(new TicketDto(), "   ", "userId must not be blank")
        );
    }

    @Test
    void getUserTickets_ShouldReturnUserTickets_givenUserRole() {
        var userId = "user-1";
        var entity = sampleEntity(userId, false);

        when(ticketRepository.findByUserId(userId)).thenReturn(List.of(entity));

        var result = ticketService.getUserTickets(userId, false);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTicketId()).isEqualTo(entity.getTicketId());
    }

    @Test
    void getUserTickets_ShouldReturnAgentTickets_givenAgentRole() {
        var agentId = "agent-1";
        var entity = sampleEntity(agentId, true);

        when(ticketRepository.findByAssigneeId(agentId)).thenReturn(List.of(entity));

        var result = ticketService.getUserTickets(agentId, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTicketId()).isEqualTo(entity.getTicketId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void getUserTickets_ShouldThrow_givenInvalidUserId(String badUserId) {
        assertThatThrownBy(() -> ticketService.getUserTickets(badUserId, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userId must not be blank");
        verifyNoInteractions(ticketRepository);
    }

    private TicketEntity sampleEntity(String id, boolean agent) {
        var e = new TicketEntity();
        e.setTicketId(UUID.randomUUID());
        e.setSubject("Sample");
        e.setDescription("Desc");
        e.setUserId(agent ? "u-1" : id);
        e.setAssigneeId(agent ? id : "a-1");
        e.setStatus(Status.OPEN);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }

    private TicketEntity buildTicketEntity(String userId, TicketDto ticketDto) {
        var entity = new TicketEntity();
        entity.setTicketId(UUID.randomUUID());
        entity.setSubject(ticketDto.getSubject());
        entity.setDescription(ticketDto.getDescription());
        entity.setUserId(userId);
        entity.setAssigneeId(ticketDto.getAssigneeId());
        entity.setStatus(Status.OPEN);
        var now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    private static TicketDto buildTicketDto() {
        var ticketDto = new TicketDto();
        ticketDto.setSubject("Test subject");
        ticketDto.setDescription("Description");
        ticketDto.setAssigneeId("agent-1");
        return ticketDto;
    }
}
