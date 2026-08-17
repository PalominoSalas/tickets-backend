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
- [Despliegue](#despliegue-deployment)
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

## � Despliegue (Deployment)

### Opción 1: Render (Recomendado - Fácil)

[Render](https://render.com) es la opción más sencilla para desplegar la aplicación.

**Requisitos:**
- Cuenta en [render.com](https://render.com)
- Repositorio en GitHub
- Base de datos PostgreSQL

**Pasos:**

1. **Crear Base de Datos en Render**
   - Ve a https://dashboard.render.com
   - Click en "New" → "PostgreSQL"
   - Nombre: `tickets-db`
   - Region: Selecciona la más cercana
   - Copia la **Internal Database URL**

2. **Desplegar el Backend**
   - Click en "New" → "Web Service"
   - Conecta tu repositorio GitHub
   - Branch: `main`
   - Build Command: `./mvnw clean install`
   - Start Command: `java -jar target/tickets-backend-0.0.1-SNAPSHOT.jar`

3. **Configurar Variables de Entorno**
   ```
   SPRING_DATASOURCE_URL=postgresql://user:pass@host:5432/tickets_db
   SPRING_DATASOURCE_USERNAME=username
   SPRING_DATASOURCE_PASSWORD=password
   APP_JWT_SECRET=tu_secret_key_muy_larga_y_segura_aqui
   SPRING_PROFILES_ACTIVE=prod
   ```

4. **Deploy automático**
   - Render desplegará automáticamente en cada push a main

**URL de la API**: `https://tickets-backend.onrender.com`

---

### Opción 2: Railway (Simple)

[Railway](https://railway.app) es muy similar a Render, también muy fácil de usar.

**Pasos:**

1. **Conectar repositorio**
   - Ve a https://railway.app
   - Click en "New Project" → "Deploy from GitHub Repo"
   - Selecciona tu repositorio

2. **Agregar PostgreSQL**
   - Click en "Add service" → "Database" → "PostgreSQL"
   - Railway lo configura automáticamente

3. **Configurar Build & Start**
   - Build command: `./mvnw clean install`
   - Start command: `java -jar target/tickets-backend-0.0.1-SNAPSHOT.jar`

4. **Variables de Entorno**
   ```
   SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
   APP_JWT_SECRET=tu_secret_aqui
   SPRING_PROFILES_ACTIVE=prod
   ```

**URL**: Railway genera una URL automáticamente

---

### Opción 3: AWS (Escalable pero más complejo)

Para aplicaciones en producción con más usuarios.

**Usando Elastic Beanstalk:**

1. **Crear Base de Datos RDS**
   ```bash
   # En AWS Console
   - RDS → Create Database → PostgreSQL
   - Nombre: tickets-db
   - Username: postgres
   - Copiar endpoint
   ```

2. **Preparar el jar**
   ```bash
   ./mvnw clean install
   # Genera: target/tickets-backend-0.0.1-SNAPSHOT.jar
   ```

3. **Desplegar a Elastic Beanstalk**
   ```bash
   # Instalar AWS CLI
   pip install awscli

   # Configurar credenciales
   aws configure

   # Crear aplicación
   eb create tickets-backend-env \
     --instance-type t3.micro \
     --database \
     --database.engine postgres \
     --database.username postgres

   # Desplegar
   eb deploy
   ```

4. **Configurar Variables de Entorno**
   ```bash
   eb setenv \
     SPRING_DATASOURCE_URL=jdbc:postgresql://endpoint:5432/tickets_db \
     SPRING_DATASOURCE_USERNAME=postgres \
     SPRING_DATASOURCE_PASSWORD=your_password \
     APP_JWT_SECRET=your_secret
   ```

**URL**: AWS proporciona una URL de Elastic Beanstalk

---

### Opción 4: Google Cloud Run (Contenedores)

Para despliegue con Docker.

1. **Crear cuenta en Google Cloud**
   - https://cloud.google.com

2. **Crear instancia PostgreSQL**
   ```bash
   gcloud sql instances create tickets-db \
     --database-version=POSTGRES_14 \
     --tier=db-f1-micro \
     --region=us-central1
   ```

3. **Construir y subir imagen Docker**
   ```bash
   # Construir
   docker build -t tickets-backend:latest .

   # Configurar Google Cloud
   gcloud auth login
   gcloud config set project YOUR_PROJECT_ID

   # Subir a Container Registry
   docker tag tickets-backend:latest \
     gcr.io/YOUR_PROJECT_ID/tickets-backend:latest

   docker push gcr.io/YOUR_PROJECT_ID/tickets-backend:latest
   ```

4. **Desplegar a Cloud Run**
   ```bash
   gcloud run deploy tickets-backend \
     --image gcr.io/YOUR_PROJECT_ID/tickets-backend:latest \
     --platform managed \
     --region us-central1 \
     --set-env-vars SPRING_DATASOURCE_URL=CONNECTION_STRING,APP_JWT_SECRET=SECRET
   ```

---

### Opción 5: Servidor VPS (DigitalOcean, Linode, Vultr)

Para máximo control.

**En DigitalOcean:**

1. **Crear Droplet**
   - Selecciona Ubuntu 22.04
   - RAM mínima: 1GB
   - Click "Create Droplet"

2. **Conectar por SSH**
   ```bash
   ssh root@your_droplet_ip
   ```

3. **Instalar dependencias**
   ```bash
   # Update
   apt update && apt upgrade -y

   # Java 21
   apt install openjdk-21-jdk -y

   # PostgreSQL
   apt install postgresql postgresql-contrib -y

   # Git
   apt install git -y

   # Nginx (reverse proxy)
   apt install nginx -y
   ```

4. **Configurar PostgreSQL**
   ```bash
   sudo -u postgres psql

   CREATE DATABASE tickets_db;
   CREATE USER tickets_user WITH PASSWORD 'strong_password';
   ALTER ROLE tickets_user SET client_encoding TO 'utf8';
   ALTER ROLE tickets_user SET default_transaction_isolation TO 'read committed';
   ALTER ROLE tickets_user SET default_transaction_deferrable TO on;
   ALTER ROLE tickets_user SET default_transaction_read_committed TO on;
   GRANT ALL PRIVILEGES ON DATABASE tickets_db TO tickets_user;
   \q
   ```

5. **Clonar y compilar**
   ```bash
   git clone https://github.com/PalominoSalas/tickets-backend.git
   cd tickets-backend
   ./mvnw clean install -DskipTests
   ```

6. **Crear servicio systemd**
   ```bash
   sudo nano /etc/systemd/system/tickets-backend.service
   ```

   Contenido:
   ```ini
   [Unit]
   Description=Tickets Backend Application
   After=network.target postgresql.service

   [Service]
   Type=simple
   User=root
   WorkingDirectory=/root/tickets-backend
   ExecStart=java -jar target/tickets-backend-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:postgresql://localhost:5432/tickets_db \
     --spring.datasource.username=tickets_user \
     --spring.datasource.password=strong_password \
     --app.jwt.secret=your_secret_key
   Restart=always
   RestartSec=10

   [Install]
   WantedBy=multi-user.target
   ```

7. **Iniciar servicio**
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable tickets-backend
   sudo systemctl start tickets-backend
   sudo systemctl status tickets-backend
   ```

8. **Configurar Nginx como Reverse Proxy**
   ```bash
   sudo nano /etc/nginx/sites-available/tickets-backend
   ```

   Contenido:
   ```nginx
   server {
       listen 80;
       server_name your_domain.com;

       location / {
           proxy_pass http://localhost:8080;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }
   }
   ```

   ```bash
   sudo ln -s /etc/nginx/sites-available/tickets-backend /etc/nginx/sites-enabled/
   sudo systemctl restart nginx
   ```

9. **SSL con Let's Encrypt**
   ```bash
   apt install certbot python3-certbot-nginx -y
   certbot --nginx -d your_domain.com
   ```

---

### Comparativa de Opciones

| Opción | Facilidad | Costo | Escalabilidad | Control |
|--------|-----------|-------|---------------|---------|
| Render | ⭐⭐⭐⭐⭐ | $7-20/mes | Media | Bajo |
| Railway | ⭐⭐⭐⭐⭐ | $5-50/mes | Media | Bajo |
| AWS | ⭐⭐⭐ | $10-100/mes | Alta | Alto |
| Google Cloud | ⭐⭐⭐ | $10-100/mes | Alta | Alto |
| DigitalOcean | ⭐⭐⭐ | $5-20/mes | Media | Muy Alto |

**Recomendación para principiantes:** Render o Railway (súper fácil)
**Recomendación para producción:** AWS o Google Cloud

---

## �📄 Licencia

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
