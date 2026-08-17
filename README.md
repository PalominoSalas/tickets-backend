# Tickets Backend

Sistema de gestión de tickets con Spring Boot 3.3.2 y PostgreSQL 14.

## Tecnologías

- **Java 21**
- **Spring Boot 3.3.2**
- **Spring Security + JWT**
- **PostgreSQL 14**
- **Flyway** para migraciones de BD
- **Maven**

## Requisitos Previos

- Java 21 o superior
- PostgreSQL 14
- Maven 3.8+

## Configuración

### 1. Variables de Entorno

Crea un archivo `.env` en la raíz:

```
DB_URL=jdbc:postgresql://localhost:5432/tickets_db
DB_USER=postgres
DB_PASSWORD=tu_contraseña
JWT_SECRET=tu_secret_key_segura_aqui
```

### 2. Base de Datos

```sql
CREATE DATABASE tickets_db;
```

Las migraciones se ejecutan automáticamente con Flyway al iniciar la aplicación.

## Instalación y Ejecución

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/tickets-backend.git
cd tickets-backend

# Compilar
./mvnw clean install

# Ejecutar
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

## Endpoints Principales

### Autenticación
- `POST /api/v1/auth/registro` - Registrar usuario
- `POST /api/v1/auth/login` - Iniciar sesión

### Tickets
- `GET /api/tickets` - Listar tickets
- `POST /api/tickets` - Crear ticket
- `GET /api/tickets/{id}` - Obtener ticket
- `PUT /api/tickets/{id}/estado` - Cambiar estado
- `PUT /api/tickets/{id}/asignar-agente` - Asignar agente

## Estructura del Proyecto

```
src/main/java/com/sistema/tickets/
├── config/
├── controller/
├── dto/
├── exception/
├── mapper/
├── model/
├── repository/
├── security/
└── service/
```

## Licencia

MIT
