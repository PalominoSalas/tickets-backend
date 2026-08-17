package com.sistema.tickets.mapper;

import com.sistema.tickets.dto.request.CrearTicketRequest;
import com.sistema.tickets.dto.response.ComentarioResponse;
import com.sistema.tickets.dto.response.HistorialEstadoResponse;
import com.sistema.tickets.dto.response.TicketResponse;
import com.sistema.tickets.dto.response.UsuarioSummaryResponse;
import com.sistema.tickets.model.Comentario;
import com.sistema.tickets.model.HistorialEstado;
import com.sistema.tickets.model.Ticket;
import com.sistema.tickets.model.Usuario;
import com.sistema.tickets.model.enums.EstadoTicket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TicketMapper {

    public Ticket toEntity(CrearTicketRequest request, Usuario cliente) {
        if (request == null) {
            return null;
        }

        return Ticket.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .prioridad(request.prioridad())
                .estado(EstadoTicket.ABIERTO)
                .cliente(cliente)
                .build();
    }

    public TicketResponse toResponse(Ticket entity) {
        if (entity == null) {
            return null;
        }

        // 1. Mapeo del Cliente
        UsuarioSummaryResponse clienteDto = toUsuarioSummary(entity.getCliente());

        // 2. Mapeo del Agente
        UsuarioSummaryResponse agenteDto = toUsuarioSummary(entity.getAgenteAsignado());

        // 3. Mapeo de Comentarios
        List<ComentarioResponse> comentariosDto = entity.getComentarios() != null
                ? entity.getComentarios().stream()
                        .map(this::toComentarioResponse)
                        .toList()
                : Collections.emptyList();

        // 4. Mapeo de Historial de Estados
        List<HistorialEstadoResponse> historialDto = entity.getHistorialEstados() != null
                ? entity.getHistorialEstados().stream()
                        .map(this::toHistorialEstadoResponse)
                        .toList()
                : Collections.emptyList();

        return new TicketResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getPrioridad(),
                clienteDto,
                agenteDto,
                comentariosDto,
                historialDto,
                entity.getFechaCreacion(),
                entity.getFechaActualizacion()
        );
    }

    // Métodos auxiliares de mapeo para mantener el código limpio:

    public UsuarioSummaryResponse toUsuarioSummary(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioSummaryResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public ComentarioResponse toComentarioResponse(Comentario comentario) {
        if (comentario == null) {
            return null;
        }
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getContenido(),                
                comentario.getEsInterno(),
                toUsuarioSummary(comentario.getAutor()), 
                comentario.getFechaCreacion()
        );
    }

    public HistorialEstadoResponse toHistorialEstadoResponse(HistorialEstado historial) {
        if (historial == null) {
            return null;
        }
        return new HistorialEstadoResponse(
                historial.getId(),
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                toUsuarioSummary(historial.getModificadoPor()), // 👈 Mapea el usuario completo como resumen
                historial.getFechaCambio()
        );
    }
}