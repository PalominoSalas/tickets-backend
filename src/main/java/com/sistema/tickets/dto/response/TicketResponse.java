package com.sistema.tickets.dto.response;

import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.enums.PrioridadTicket;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TicketResponse(
        Long id,
        String titulo,
        String descripcion,
        EstadoTicket estado,
        PrioridadTicket prioridad,
        UsuarioSummaryResponse cliente,
        UsuarioSummaryResponse agenteAsignado,
        List<ComentarioResponse> comentarios,
        List<HistorialEstadoResponse> historialEstados,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {}