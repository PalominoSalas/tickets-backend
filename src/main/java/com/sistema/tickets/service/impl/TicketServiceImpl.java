package com.sistema.tickets.service.impl;

import com.sistema.tickets.dto.request.AsignarAgenteRequest;
import com.sistema.tickets.dto.request.CambiarEstadoRequest;
import com.sistema.tickets.dto.request.CrearTicketRequest;
import com.sistema.tickets.dto.response.TicketResponse;
import com.sistema.tickets.dto.response.UsuarioSummaryResponse;
import com.sistema.tickets.exception.ResourceNotFoundException;
import com.sistema.tickets.model.Comentario;
import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.HistorialEstado;
import com.sistema.tickets.model.enums.PrioridadTicket;
import com.sistema.tickets.model.enums.Rol;
import com.sistema.tickets.model.Ticket;
import com.sistema.tickets.model.Usuario;
import com.sistema.tickets.repository.ComentarioRepository;
import com.sistema.tickets.repository.HistorialEstadoRepository;
import com.sistema.tickets.repository.TicketRepository;
import com.sistema.tickets.repository.UsuarioRepository;
import com.sistema.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialEstadoRepository historialEstadoRepository;
    private final ComentarioRepository comentarioRepository;

    @Override
    @Transactional
    public TicketResponse crearTicket(CrearTicketRequest request, String emailUsuario) {
        Usuario cliente = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Ticket ticket = new Ticket();
        ticket.setTitulo(request.titulo());
        ticket.setDescripcion(request.descripcion());
        ticket.setPrioridad(request.prioridad());
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setCliente(cliente);

        Ticket guardado = ticketRepository.save(ticket);
        return mapToResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse obtenerPorId(Long id, String emailUsuario) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + id));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        validarAccesoTicket(ticket, usuario);

        return mapToResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> filtrarTickets(EstadoTicket estado, PrioridadTicket prioridad, Long clienteId, Long agenteId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usuario.getRol() == Rol.CLIENTE) {
            clienteId = usuario.getId();
        }

        List<Ticket> tickets = ticketRepository.filtrarTickets(estado, prioridad, clienteId, agenteId);
        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketResponse cambiarEstado(Long id, CambiarEstadoRequest request, String emailUsuario) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + id));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        validarAccesoTicket(ticket, usuario);

        EstadoTicket estadoAnterior = ticket.getEstado();
        EstadoTicket estadoNuevo = request.nuevoEstado();

        if (estadoAnterior != estadoNuevo) {
            ticket.setEstado(estadoNuevo);

            HistorialEstado historial = new HistorialEstado();
            historial.setTicket(ticket);
            historial.setEstadoAnterior(estadoAnterior);
            historial.setEstadoNuevo(estadoNuevo);
            historial.setModificadoPor(usuario);
            historialEstadoRepository.save(historial);

            Ticket actualizado = ticketRepository.save(ticket);

            // Crear comentario automático interno registrando el cambio de estado
            Comentario comentario = new Comentario();
            comentario.setContenido(request.comentarioOpcional() != null ? request.comentarioOpcional() : "Cambio de estado de " + estadoAnterior + " a " + estadoNuevo);
            comentario.setTicket(actualizado);
            comentario.setAutor(usuario);
            comentario.setEsInterno(true);
            comentarioRepository.save(comentario);

            return mapToResponse(actualizado);
        }

        return mapToResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse asignarAgente(Long id, AsignarAgenteRequest request, String emailUsuario) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + id));

        Usuario agente = usuarioRepository.findById(request.agenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Agente no encontrado con el id: " + request.agenteId()));

        Usuario usuarioActual = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (agente.getRol() != Rol.AGENTE && agente.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("El usuario asignado debe tener rol AGENTE o ADMIN");
        }

        ticket.setAgenteAsignado(agente);
        Ticket actualizado = ticketRepository.save(ticket);

        // Crear comentario automático interno registrando la asignación
        Comentario comentario = new Comentario();
        comentario.setContenido("Agente asignado: " + agente.getNombre() + " (ID: " + agente.getId() + ")");
        comentario.setTicket(actualizado);
        comentario.setAutor(usuarioActual);
        comentario.setEsInterno(true);
        comentarioRepository.save(comentario);

        return mapToResponse(actualizado);
    }

    private void validarAccesoTicket(Ticket ticket, Usuario usuario) {
        if (usuario.getRol() == Rol.CLIENTE && !ticket.getCliente().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No tiene permiso para acceder a este ticket");
        }
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        UsuarioSummaryResponse clienteResponse = ticket.getCliente() != null
                ? new UsuarioSummaryResponse(
                        ticket.getCliente().getId(),
                        ticket.getCliente().getNombre(),
                        ticket.getCliente().getEmail(),
                        ticket.getCliente().getRol())
                : null;

        UsuarioSummaryResponse agenteResponse = ticket.getAgenteAsignado() != null
                ? new UsuarioSummaryResponse(
                        ticket.getAgenteAsignado().getId(),
                        ticket.getAgenteAsignado().getNombre(),
                        ticket.getAgenteAsignado().getEmail(),
                        ticket.getAgenteAsignado().getRol())
                : null;

        return TicketResponse.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado())
                .prioridad(ticket.getPrioridad())
                .cliente(clienteResponse)
                .agenteAsignado(agenteResponse)
                .comentarios(Collections.emptyList())
                .historialEstados(Collections.emptyList())
                .fechaCreacion(ticket.getFechaCreacion())
                .fechaActualizacion(ticket.getFechaActualizacion())
                .build();
    }
}