package com.sistema.tickets.repository;

import com.sistema.tickets.model.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    // Obtener la traza histórica completa del cambio de estados de un ticket
    @Query("""
        SELECT h FROM HistorialEstado h 
        JOIN FETCH h.modificadoPor 
        WHERE h.ticket.id = :ticketId 
        ORDER BY h.fechaCambio DESC
    """)
    List<HistorialEstado> findByTicketIdOrderByFechaCambioDesc(@Param("ticketId") Long ticketId);
}