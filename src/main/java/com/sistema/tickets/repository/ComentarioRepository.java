package com.sistema.tickets.repository;

import com.sistema.tickets.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Comentarios visibles para el cliente (se excluyen notas internas)
    List<Comentario> findByTicketIdAndEsInternoFalseOrderByFechaCreacionAsc(Long ticketId);

    // Todos los comentarios del ticket (incluye notas internas para Soporte/Admin)
    @Query("""
        SELECT c FROM Comentario c 
        JOIN FETCH c.autor 
        WHERE c.ticket.id = :ticketId 
        ORDER BY c.fechaCreacion ASC
    """)
    List<Comentario> findAllByTicketIdConAutor(@Param("ticketId") Long ticketId);
}