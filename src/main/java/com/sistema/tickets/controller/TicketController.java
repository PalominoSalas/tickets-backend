package com.sistema.tickets.controller;

import com.sistema.tickets.dto.request.AsignarAgenteRequest;
import com.sistema.tickets.dto.request.CambiarEstadoRequest;
import com.sistema.tickets.dto.request.CrearTicketRequest;
import com.sistema.tickets.dto.response.TicketResponse;
import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.enums.PrioridadTicket;
import com.sistema.tickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<TicketResponse> crearTicket(
            @Valid @RequestBody CrearTicketRequest request,
            Authentication authentication) {
        TicketResponse ticket = ticketService.crearTicket(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'AGENTE', 'ADMIN')")
    public ResponseEntity<TicketResponse> obtenerPorId(
            @PathVariable Long id,
            Authentication authentication) {
        TicketResponse ticket = ticketService.obtenerPorId(id, authentication.getName());
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'AGENTE', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> filtrarTickets(
            @RequestParam(required = false) EstadoTicket estado,
            @RequestParam(required = false) PrioridadTicket prioridad,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long agenteId,
            Authentication authentication) {
        List<TicketResponse> tickets = ticketService.filtrarTickets(
                estado, prioridad, clienteId, agenteId, authentication.getName());
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('CLIENTE', 'AGENTE', 'ADMIN')")
    public ResponseEntity<TicketResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequest request,
            Authentication authentication) {
        TicketResponse ticket = ticketService.cambiarEstado(id, request, authentication.getName());
        return ResponseEntity.ok(ticket);
    }

    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasAnyRole('AGENTE', 'ADMIN')")
    public ResponseEntity<TicketResponse> asignarAgente(
            @PathVariable Long id,
            @Valid @RequestBody AsignarAgenteRequest request,
            Authentication authentication) {
        TicketResponse ticket = ticketService.asignarAgente(id, request, authentication.getName());
        return ResponseEntity.ok(ticket);
    }
}