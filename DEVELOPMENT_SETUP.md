# 🛠️ Guía Completa de Setup Local - Backend

**Última actualización**: 2026-08-16  
**Versión**: 1.0.0  
**Duración estimada**: 30 minutos

> ⚠️ **IMPORTANTE**: Sigue EXACTAMENTE estos pasos en el orden indicado. Si algo no funciona, ve a la sección [Troubleshooting](#troubleshooting).

---

## 📋 Tabla de Contenidos

- [Requisitos Previos](#requisitos-previos)
- [Paso 1: Clonar el Repositorio](#paso-1-clonar-el-repositorio)
- [Paso 2: Instalar Dependencias](#paso-2-instalar-dependencias)
- [Paso 3: Configurar PostgreSQL](#paso-3-configurar-postgresql)
- [Paso 4: Configurar Variables de Entorno](#paso-4-configurar-variables-de-entorno)
- [Paso 5: Ejecutar Migraciones de BD](#paso-5-ejecutar-migraciones-de-bd)
- [Paso 6: Compilar el Proyecto](#paso-6-compilar-el-proyecto)
- [Paso 7: Ejecutar la Aplicación](#paso-7-ejecutar-la-aplicación)
- [Paso 8: Verificar que Funciona](#paso-8-verificar-que-funciona)
- [Troubleshooting](#troubleshooting)
- [Comandos Útiles](#comandos-útiles)

---

## ✅ Requisitos Previos

Antes de comenzar, asegúrate de tener instalados:

### 1. Java 21 LTS

**Verificar versión instalada:**
```bash
java -version
```

**Salida esperada:**
```
openjdk version "21" 2023-09-19 LTS
```

**Si NO está instalado:**
- **Windows**: Descarga de https://www.oracle.com/java/technologies/downloads/#java21
- **Mac**: `brew install openjdk@21`
- **Linux**: `sudo apt install openjdk-21-jdk`

✅ **Verifica nuevamente**: `java -version`

---

### 2. PostgreSQL 14+

**Verificar versión instalada:**
```bash
psql --version
```

**Salida esperada:**
```
psql (PostgreSQL) 14.5
```

**Si NO está instalado:**
- **Windows**: Descarga de https://www.postgresql.org/download/windows/
- **Mac**: `brew install postgresql@14`
- **Linux**: `sudo apt install postgresql-14`

✅ **Verifica**: `psql --version`

---

### 3. Git

**Verificar versión instalada:**
```bash
git --version
```

**Salida esperada:**
```
git version 2.40.0
```

**Si NO está instalado:**
- Descarga de https://git-scm.com/downloads

✅ **Verifica**: `git --version`

---

### 4. Maven 3.8+

**Verificar versión instalada:**
```bash
mvn --version
```

**Salida esperada:**
```
Apache Maven 3.8.1
```

**Si NO está instalado:**
- Descarga de https://maven.apache.org/download.cgi

✅ **Verifica**: `mvn --version`

---

### 5. Node.js 18+ (Para usar frontend después)

**Verificar versión instalada:**
```bash
node --version
npm --version
```

**Salida esperada:**
```
v18.17.0
9.6.7
```

**Si NO está instalado:**
- Descarga de https://nodejs.org/

✅ **Verifica**: `node --version`

---

## 🚀 Paso 1: Clonar el Repositorio

```bash
# Navega a donde quieras guardar el proyecto
cd C:\tu\ruta\deseada

# Clona el repositorio
git clone https://github.com/PalominoSalas/tickets-backend.git

# Entra a la carpeta
cd tickets-backend

# Verifica que clonó correctamente
dir
# Deberías ver: pom.xml, src/, docker-compose.yml, etc.
```

✅ **Verifica**: Puedes ver la carpeta con los archivos del proyecto

---

## 🔧 Paso 2: Instalar Dependencias

**En la carpeta del proyecto** (donde está `pom.xml`):

```bash
# Compilar y descargar todas las dependencias
./mvnw clean install -DskipTests

# Si estás en Mac/Linux:
# ./mvnw clean install -DskipTests
```

⏳ **Esto toma 3-5 minutos la primera vez**

✅ **Verifica**: Al final deberías ver:
```
[INFO] BUILD SUCCESS
```

**Si ves error**:
- Ve a la sección [BUILD ERROR](#build-error) en Troubleshooting

---

## 🗄️ Paso 3: Configurar PostgreSQL

### 3.1 Iniciar el servicio PostgreSQL

**Windows:**
```bash
# PostgreSQL debería estar corriendo como servicio
# Verifica en Servicios (services.msc) que "postgresql-x64" esté corriendo
```

**Mac:**
```bash
brew services start postgresql@14
```

**Linux:**
```bash
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### 3.2 Conectar a PostgreSQL

```bash
# Conectar como usuario postgres (contraseña por defecto durante instalación)
psql -U postgres
```

Si pide contraseña y no la sabes:
- Ve a [No puedo conectar a PostgreSQL](#no-puedo-conectar-a-postgresql) en Troubleshooting

### 3.3 Crear la Base de Datos y Usuario

En la consola de `psql`, ejecuta:

```sql
-- Crear base de datos
CREATE DATABASE tickets_db;

-- Crear usuario específico
CREATE USER tickets_user WITH PASSWORD 'tickets_password_123';

-- Dar permisos
ALTER ROLE tickets_user SET client_encoding TO 'utf8';
ALTER ROLE tickets_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE tickets_user SET default_transaction_deferrable TO on;
GRANT ALL PRIVILEGES ON DATABASE tickets_db TO tickets_user;

-- Salir
\q
```

✅ **Verifica**: Conecta como el nuevo usuario:
```bash
psql -U tickets_user -d tickets_db -h localhost
```

Debería conectar sin errores. Luego escribe `\q` para salir.

---

## ⚙️ Paso 4: Configurar Variables de Entorno

### 4.1 Crear archivo de configuración

En la carpeta del proyecto, crea: `src/main/resources/application-local.yaml`

```bash
# En Windows (usando PowerShell)
New-Item -Path "src/main/resources/application-local.yaml" -Type File -Force

# En Mac/Linux
touch src/main/resources/application-local.yaml
```

### 4.2 Completar el archivo

Abre el archivo y copia este contenido:

```yaml
spring:
  application:
    name: tickets-backend
  
  datasource:
    url: jdbc:postgresql://localhost:5432/tickets_db
    username: tickets_user
    password: tickets_password_123
    driverClassName: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  flyway:
    baseline-on-migrate: true
    locations: classpath:db/migration

server:
  port: 8080
  servlet:
    context-path: /

app:
  jwt:
    secret: mi_secret_key_super_segura_minimo_32_caracteres_aqui_12345
    expiration: 86400000  # 24 horas en milisegundos
    token-header: Authorization
    token-prefix: Bearer 

logging:
  level:
    root: INFO
    com.sistema.tickets: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

⚠️ **IMPORTANTE**:
- `secret` debe tener mínimo 32 caracteres
- `password` debe ser `tickets_password_123` (la que creaste en PostgreSQL)
- No cambies `url`, `username`, `driverClassName` si seguiste los pasos

✅ **Verifica**: El archivo está creado en la ruta correcta:
```
tickets-backend/src/main/resources/application-local.yaml
```

---

## 🗄️ Paso 5: Ejecutar Migraciones de BD

Flyway ejecutará las migraciones automáticamente al iniciar. Los archivos están en:
- `src/main/resources/db/migration/V1__init_schema.sql`
- `src/main/resources/db/migration/V2__add_agente_rol.sql`

✅ **Verifica**: Las migraciones se ejecutarán cuando inicies la app (próximo paso)

---

## 🔨 Paso 6: Compilar el Proyecto

En la carpeta del proyecto:

```bash
# Compilar sin ejecutar tests
./mvnw clean compile

# O si quieres compilar + tests
./mvnw clean install
```

⏳ **Toma 2-3 minutos**

✅ **Verifica**: Al final ves `BUILD SUCCESS`

---

## ▶️ Paso 7: Ejecutar la Aplicación

### Opción A: Con Maven (Recomendado para desarrollo)

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

### Opción B: Compilar y ejecutar el JAR

```bash
# Compilar
./mvnw clean install -DskipTests

# Ejecutar
java -jar target/tickets-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

⏳ **Toma 10-15 segundos en iniciar**

✅ **Verifica**: Deberías ver en la consola:
```
...
2026-08-16 10:30:45.123  INFO 12345 --- [main] com.sistema.tickets.TicketsBackendApplication : 
Starting TicketsBackendApplication v0.0.1-SNAPSHOT using Java 21...
...
2026-08-16 10:30:50.456  INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : 
Tomcat started on port(s): 8080 (http) with context path ''
...
```

**Si NO ves esto**: Ve a [Aplicación no inicia](#aplicación-no-inicia) en Troubleshooting

---

## ✔️ Paso 8: Verificar que Funciona

### 8.1 Verificar que el servidor está corriendo

Abre otra consola y ejecuta:

```bash
curl http://localhost:8080/swagger-ui/index.html
```

O en el navegador:
```
http://localhost:8080/swagger-ui/index.html
```

✅ **Deberías ver**: La interfaz de Swagger UI

### 8.2 Registrar un usuario de prueba

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "TestPassword123!",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "rol": "CLIENTE"
  }'
```

✅ **Respuesta esperada**:
```json
{
  "id": 1,
  "username": "test@example.com",
  "nombres": "Juan",
  "apellidos": "Pérez",
  "rol": "CLIENTE"
}
```

### 8.3 Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "TestPassword123!"
  }'
```

✅ **Respuesta esperada**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "username": "test@example.com",
    "rol": "CLIENTE"
  }
}
```

**Guarda el token**, lo necesitarás para siguientes requests.

### 8.4 Crear un ticket

```bash
curl -X POST http://localhost:8080/api/v1/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "titulo": "Pantalla negra",
    "descripcion": "La aplicación muestra pantalla negra",
    "prioridad": "ALTA"
  }'
```

✅ **Deberías recibir el ticket creado**

---

## 🐛 Troubleshooting

### BUILD ERROR

**Error**: `[ERROR] COMPILATION ERROR`

**Causa**: Falta Java o versión incorrecta

**Solución**:
```bash
# Verifica versión
java -version

# Debe ser Java 21 o mayor
# Si no es, instala Java 21 de:
# https://www.oracle.com/java/technologies/downloads/#java21

# Después, en PowerShell (admin):
# Configura JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
echo $env:JAVA_HOME
```

---

### No puedo conectar a PostgreSQL

**Error**: `FATAL: role "postgres" does not exist`

**Solución - Windows**:
```bash
# 1. Abre pgAdmin (que instalaste con PostgreSQL)
# 2. En el navegador: http://localhost:5050
# 3. Click derecho en "Servers" → Register → Server
# 4. Nombre: localhost
# 5. Host: localhost, Port: 5432
# 6. Username: postgres, Password: (la que pusiste en instalación)
```

**Solución - Mac/Linux**:
```bash
# Reinicia PostgreSQL
brew services restart postgresql@14

# O
sudo systemctl restart postgresql
```

---

### Error de conexión a Base de Datos

**Error**: `Connection refused: localhost:5432`

**Causa**: PostgreSQL no está corriendo

**Solución**:
```bash
# Windows: Verifica en Servicios (services.msc) que postgresql esté corriendo
# Mac: brew services start postgresql@14
# Linux: sudo systemctl start postgresql

# Verifica que conecta
psql -U postgres
```

---

### Error: "database tickets_db does not exist"

**Causa**: No creaste la base de datos

**Solución**:
```bash
# Conecta a PostgreSQL
psql -U postgres

# Ejecuta (dentro de psql):
CREATE DATABASE tickets_db;
CREATE USER tickets_user WITH PASSWORD 'tickets_password_123';
GRANT ALL PRIVILEGES ON DATABASE tickets_db TO tickets_user;
\q
```

---

### Aplicación no inicia

**Error**: `Failed to configure a DataSource`

**Causa**: Configuración incorrecta

**Solución**:
```bash
# 1. Verifica el archivo application-local.yaml existe:
#    tickets-backend/src/main/resources/application-local.yaml

# 2. Verifica que contiene:
#    url: jdbc:postgresql://localhost:5432/tickets_db
#    username: tickets_user
#    password: tickets_password_123

# 3. Verifica que PostgreSQL está corriendo:
psql -U tickets_user -d tickets_db

# 4. Intenta iniciar con logs detallados:
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local --debug"
```

---

### Puerto 8080 ya está en uso

**Error**: `Failed to initialize endpoint`

**Solución**:
```bash
# Opción 1: Mata el proceso en ese puerto
# Windows (PowerShell admin):
netstat -ano | findstr :8080
taskkill /PID 12345 /F

# Mac/Linux:
lsof -i :8080
kill -9 12345

# Opción 2: Cambia el puerto en application-local.yaml:
server:
  port: 8081  # Cambia a otro puerto
```

---

### Test falla: "Cannot connect to database"

**Causa**: La BD de test no existe

**Solución**:
```bash
# Crea BD de test
psql -U postgres -c "CREATE DATABASE tickets_test;"

# O simplemente salta los tests:
./mvnw install -DskipTests
```

---

## 📚 Comandos Útiles

### Desarrollo

```bash
# Iniciar la aplicación en modo desarrollo
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"

# Compilar sin ejecutar
./mvnw clean compile

# Compilar + tests
./mvnw clean install

# Ejecutar solo tests
./mvnw test

# Limpiar (elimina carpeta target/)
./mvnw clean
```

### Base de Datos

```bash
# Conectar a la BD
psql -U tickets_user -d tickets_db

# Ver todas las tablas (dentro de psql)
\dt

# Ver estructura de tabla
\d usuario

# Ver datos de tabla
SELECT * FROM usuario;

# Salir de psql
\q

# Backup de la BD
pg_dump -U tickets_user tickets_db > backup.sql

# Restaurar desde backup
psql -U tickets_user tickets_db < backup.sql
```

### Git

```bash
# Ver cambios locales
git status

# Crear rama nueva
git checkout -b feature/nueva-funcionalidad

# Ver cambios
git diff

# Commitear cambios
git add .
git commit -m "feat: Descripción de cambios"

# Subir cambios
git push origin feature/nueva-funcionalidad
```

---

## 📞 ¿Algo no funciona?

### Checklist de verificación:

- [ ] Java 21 instalado: `java -version`
- [ ] PostgreSQL corriendo: `psql -U postgres`
- [ ] Base de datos creada: `psql -U tickets_user -d tickets_db`
- [ ] Archivo `application-local.yaml` en `src/main/resources/`
- [ ] Credenciales correctas en `application-local.yaml`
- [ ] Puerto 8080 disponible: `netstat -ano | findstr :8080`
- [ ] Proyecto compilado: `./mvnw clean compile`

### Si aún tienes problemas:

1. **Copia el error exacto** de la consola
2. **Ve a la sección Troubleshooting** que más se parezca
3. **Sigue los pasos exactamente** como están escritos
4. **Verifica cada paso** con los comandos de verificación (✅)

---

## 🎉 ¡Listo!

Si completaste todos los pasos sin errores, tienes:

✅ Backend corriendo en `http://localhost:8080`
✅ Base de datos conectada y funcionando
✅ API disponible en Swagger: `http://localhost:8080/swagger-ui/`
✅ Sistema de autenticación JWT operativo

**Próximo paso**: Configura el Frontend (ver `DEVELOPMENT_SETUP.md` en `tickets-frontend`)

---

## 📝 Notas Importantes

- **NUNCA** commits credenciales o secrets al repositorio
- El archivo `application-local.yaml` está en `.gitignore`
- Los tests incluyen la BD de test: `tickets_test`
- Flyway ejecuta migraciones automáticamente

---

**¿Preguntas? Contacta al equipo**
