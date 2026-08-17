package com.sistema.tickets.repository;

import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.enums.PrioridadTicket;
import com.sistema.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByClienteId(Long clienteId);

    List<Ticket> findByAgenteAsignadoId(Long agenteId);

    List<Ticket> findByEstado(EstadoTicket estado);

    @Query("""
           SELECT t FROM Ticket t WHERE 
           (:estado IS NULL OR t.estado = :estado) AND 
           (:prioridad IS NULL OR t.prioridad = :prioridad) AND 
           (:clienteId IS NULL OR t.cliente.id = :clienteId) AND 
           (:agenteId IS NULL OR t.agenteAsignado.id = :agenteId)
           """)
    List<Ticket> filtrarTickets(
            @Param("estado") EstadoTicket estado,
            @Param("prioridad") PrioridadTicket prioridad,
            @Param("clienteId") Long clienteId,
            @Param("agenteId") Long agenteId
    );
}