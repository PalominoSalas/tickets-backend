# 🎫 Tickets Backend

Sistema de gestión de tickets empresarial con **Spring Boot 3.3.2**, **JWT** y **PostgreSQL 14**. Proporciona una API REST completa para la creación, seguimiento y resolución de tickets con soporte para roles de usuario (cliente, agente, administrador).

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [API Endpoints](#api-endpoints)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Contribución](#contribución)

## ✨ Características

- ✅ **Autenticación JWT** - Token seguro con expiración configurable
- ✅ **Control de Roles** - Cliente, Agente, Admin
- ✅ **Gestión de Tickets** - Crear, actualizar, cambiar estado
- ✅ **Historial de Estados** - Seguimiento completo de cambios
- ✅ **Comentarios** - Comunicación entre usuarios
- ✅ **Asignación de Agentes** - Distribución de tickets
- ✅ **Validaciones Complejas** - Estados permitidos por rol
- ✅ **Documentación OpenAPI** - Swagger UI integrado
- ✅ **Manejo Global de Excepciones** - Respuestas consistentes
- ✅ **Migraciones con Flyway** - Versionado de BD

## 🛠 Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Java | 21 LTS | Lenguaje de programación |
| Spring Boot | 3.3.2 | Framework principal |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Acceso a datos |
| JWT (jjwt) | 0.12.x | Generación de tokens |
| PostgreSQL | 14+ | Base de datos |
| Flyway | 9.x | Migraciones de BD |
| Lombok | 1.18 | Reducción de código boilerplate |
| Maven | 3.8+ | Gestor de dependencias |
| Docker | Latest | Contenedores |

## 📦 Requisitos

- **Java 21 LTS** o superior
- **PostgreSQL 14** o superior
- **Maven 3.8+**
- **Git**
- **Docker** (opcional, para ejecutar en contenedores)

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/PalominoSalas/tickets-backend.git
cd tickets-backend
```

### 2. Configurar Variables de Entorno

Crea un archivo `application-local.yaml` en `src/main/resources/`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tickets_db
    username: postgres
    password: tu_contraseña
    driverClassName: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
  
  flyway:
    baseline-on-migrate: true

app:
  jwt:
    secret: tu_secret_key_segura_minimo_32_caracteres_aqui
    expiration: 86400000  # 24 horas en ms
```

### 3. Crear la Base de Datos

```sql
CREATE DATABASE tickets_db;
```

## ⚙️ Configuración

### JWT Configuration

El archivo `SecurityConfig.java` configura:
- Filtro JWT personalizado
- CORS habilitado
- Rutas públicas vs protegidas

### Propiedades Principales (application.yaml)

```yaml
app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: ${JWT_EXPIRATION:86400000}
  
  api:
    base-path: /api/v1
```

## ▶️ Ejecución

### Con Maven (Desarrollo)

```bash
# Compilar y ejecutar
./mvnw clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"

# Solo compilar
./mvnw clean install
```

### Con Docker

```bash
# Construir imagen
docker build -t tickets-backend:latest .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/tickets_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e APP_JWT_SECRET=tu_secret_aqui \
  tickets-backend:latest
```

### Docker Compose

```bash
docker-compose up -d
```

La aplicación estará disponible en `http://localhost:8080`

**Documentación API**: http://localhost:8080/swagger-ui/index.html

## 📡 API Endpoints

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Registrar nuevo usuario |
| POST | `/api/v1/auth/login` | Iniciar sesión |

### Tickets

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/tickets` | Listar todos los tickets |
| GET | `/api/v1/tickets/{id}` | Obtener ticket por ID |
| POST | `/api/v1/tickets` | Crear nuevo ticket |
| PUT | `/api/v1/tickets/{id}/estado` | Cambiar estado |
| PUT | `/api/v1/tickets/{id}/asignar` | Asignar agente |

### Comentarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/tickets/{id}/comentarios` | Listar comentarios |
| POST | `/api/v1/tickets/{id}/comentarios` | Agregar comentario |

### Historial

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/tickets/{id}/historial` | Ver historial de cambios |

## 📁 Estructura del Proyecto

```
src/main/
├── java/com/sistema/tickets/
│   ├── TicketsBackendApplication.java          # Clase principal
│   ├── config/
│   │   ├── OpenApiConfig.java                  # Configuración Swagger
│   │   └── SecurityConfig.java                 # Seguridad y JWT
│   ├── controller/
│   │   ├── AuthController.java                 # Endpoints de autenticación
│   │   └── TicketController.java               # Endpoints de tickets
│   ├── dto/
│   │   ├── request/                            # DTOs de entrada
│   │   └── response/                           # DTOs de salida
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java         # Manejo de errores
│   │   └── Custom*Exception.java               # Excepciones personalizadas
│   ├── mapper/
│   │   └── TicketMapper.java                   # Conversión entre entidades y DTOs
│   ├── model/
│   │   ├── Usuario.java                        # Entidad usuario
│   │   ├── Ticket.java                         # Entidad ticket
│   │   ├── Comentario.java                     # Entidad comentario
│   │   ├── HistorialEstado.java                # Entidad historial
│   │   └── enums/                              # Enumeraciones
│   ├── repository/
│   │   ├── UsuarioRepository.java              # Acceso a usuarios
│   │   ├── TicketRepository.java               # Acceso a tickets
│   │   ├── ComentarioRepository.java           # Acceso a comentarios
│   │   └── HistorialEstadoRepository.java      # Acceso a historial
│   ├── security/
│   │   ├── UserPrincipal.java                  # Usuario autenticado
│   │   ├── CustomUserDetailsService.java       # Carga de usuarios
│   │   └── jwt/
│   │       ├── JwtUtils.java                   # Utilidades JWT
│   │       └── JwtAuthenticationFilter.java    # Filtro JWT
│   └── service/
│       ├── AuthService.java                    # Interfaz servicio auth
│       ├── TicketService.java                  # Interfaz servicio tickets
│       └── impl/
│           ├── AuthServiceImpl.java             # Implementación auth
│           └── TicketServiceImpl.java           # Implementación tickets
├── resources/
│   ├── application.yaml                        # Configuración principal
│   └── db/migration/
│       ├── V1__init_schema.sql                 # Creación de tablas
│       └── V2__add_agente_rol.sql              # Nuevas funcionalidades
└── test/
    └── java/                                   # Tests unitarios
```

## 💡 Ejemplos de Uso

### 1. Registrar Usuario

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan@example.com",
    "password": "password123",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "rol": "CLIENTE"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan@example.com",
    "password": "password123"
  }'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "username": "juan@example.com",
    "rol": "CLIENTE"
  }
}
```

### 3. Crear Ticket

```bash
curl -X POST http://localhost:8080/api/v1/tickets \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "El sistema no responde",
    "descripcion": "La aplicación se congela al hacer clic en...",
    "prioridad": "ALTA"
  }'
```

### 4. Cambiar Estado del Ticket

```bash
curl -X PUT http://localhost:8080/api/v1/tickets/1/estado \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nuevoEstado": "EN_PROGRESO"
  }'
```

## 🧪 Testing

```bash
# Ejecutar todos los tests
./mvnw test

# Con cobertura
./mvnw jacoco:report
```

## 🔐 Seguridad

- Todos los endpoints excepto `/auth/**` requieren autenticación
- Las contraseñas se almacenan hasheadas con BCrypt
- Los tokens JWT expiran después del tiempo configurado
- CORS está habilitado solo para localhost en desarrollo
- Las validaciones se hacen a nivel de servidor

## 📝 Convenciones

- **Nombres de clases**: PascalCase (UsuarioController.java)
- **Métodos**: camelCase (crearTicket())
- **Constantes**: UPPER_SNAKE_CASE (MAX_TICKETS)
- **Bases de datos**: snake_case (usuario_tickets)

## 🐛 Troubleshooting

### Error: "Cannot connect to database"
- Verifica que PostgreSQL está corriendo
- Comprueba las credenciales en application.yaml
- Asegúrate que la BD existe

### Error: "Invalid JWT token"
- Regenera el token con login
- Verifica el JWT_SECRET sea el mismo en config

### Error: "Access Denied"
- Comprueba que el usuario tiene el rol requerido
- Verifica que el token no haya expirado

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

## 📧 Contacto

**Autor**: Sistema de Tickets  
**Email**: contacto@tickets.com

---

⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub!

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
