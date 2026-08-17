package com.sistema.tickets.service;

import com.sistema.tickets.dto.request.AsignarAgenteRequest;
import com.sistema.tickets.dto.request.CambiarEstadoRequest;
import com.sistema.tickets.dto.request.CrearTicketRequest;
import com.sistema.tickets.dto.response.TicketResponse;
import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.enums.PrioridadTicket;

import java.util.List;

public interface TicketService {

    TicketResponse crearTicket(CrearTicketRequest request, String emailUsuario);

    TicketResponse obtenerPorId(Long id, String emailUsuario);

    List<TicketResponse> filtrarTickets(EstadoTicket estado, PrioridadTicket prioridad, Long clienteId, Long agenteId, String emailUsuario);

    TicketResponse cambiarEstado(Long id, CambiarEstadoRequest request, String emailUsuario);

    TicketResponse asignarAgente(Long id, AsignarAgenteRequest request, String emailUsuario);
}