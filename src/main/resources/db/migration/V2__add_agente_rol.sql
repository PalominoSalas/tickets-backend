-- =============================================================================
-- V2__add_agente_rol.sql
-- AGREGAR ROL AGENTE A LA RESTRICCIÓN CHECK DE LA TABLA USUARIOS
-- =============================================================================

ALTER TABLE usuarios DROP CONSTRAINT usuarios_rol_check;
ALTER TABLE usuarios ADD CHECK (rol IN ('CLIENTE', 'SOPORTE', 'ADMIN', 'AGENTE'));
