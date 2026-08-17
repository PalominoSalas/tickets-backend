# ==========================================
# Etapa 1: Build (Compilacion con Maven)
# ==========================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiamos pom.xml y descargamos dependencias (aprovecha la cache de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el codigo fuente y construimos el JAR omitiendo tests
COPY src ./src
RUN mvn package -DskipTests

# ==========================================
# Etapa 2: Runtime (Imagen final ligera)
# ==========================================
FROM eclipse-temurin:17-jr-alpine
WORKDIR /app

# Crear usuario no root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar el ejecutable empaquetado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puertos expuestos
EXPOSE 8080

# Comando de ejecucion
ENTRYPOINT ["java", "-jar", "app.jar"]