-- =============================================================================
-- V1__init_schema.sql
-- ESTRUCTURA INICIAL DE TABLAS E ÍNDICES - SISTEMA DE TICKETS
-- =============================================================================

-- 1. Tabla: Usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('CLIENTE', 'SOPORTE', 'ADMIN')),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla: Tickets
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO' CHECK (estado IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO', 'CERRADO')),
    prioridad VARCHAR(20) NOT NULL CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA')),
    cliente_id BIGINT NOT NULL,
    agente_asignado_id BIGINT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tickets_cliente FOREIGN KEY (cliente_id)
        REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_tickets_agente FOREIGN KEY (agente_asignado_id)
        REFERENCES usuarios(id) ON DELETE SET NULL
);

-- 3. Tabla: Comentarios
CREATE TABLE comentarios (
    id BIGSERIAL PRIMARY KEY,
    contenido TEXT NOT NULL,
    ticket_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    es_interno BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comentarios_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_comentarios_autor FOREIGN KEY (autor_id)
        REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- 4. Tabla: Historial de Estados
CREATE TABLE historial_estados (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    estado_anterior VARCHAR(20) CHECK (estado_anterior IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO', 'CERRADO')),
    estado_nuevo VARCHAR(20) NOT NULL CHECK (estado_nuevo IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO', 'CERRADO')),
    modificado_por_id BIGINT NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historial_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_historial_modificado_por FOREIGN KEY (modificado_por_id)
        REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- =============================================================================
-- ÍNDICES PARA BÚSQUEDAS Y FILTROS FRECUENTES
-- =============================================================================

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_tickets_cliente_id ON tickets(cliente_id);
CREATE INDEX idx_tickets_agente_asignado_id ON tickets(agente_asignado_id);
CREATE INDEX idx_tickets_estado_prioridad ON tickets(estado, prioridad);
CREATE INDEX idx_comentarios_ticket_id ON comentarios(ticket_id);
CREATE INDEX idx_historial_ticket_id ON historial_estados(ticket_id);