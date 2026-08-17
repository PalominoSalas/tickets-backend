# 🚀 Guía Rápida: Setup Completo Backend + Frontend

**Para desarrolladores que quieren empezar de cero con el proyecto**

> Sigue esta guía si clonaste AMBOS proyectos y quieres que funcionen juntos

---

## ⏱️ Tiempo Total Estimado: 45 minutos

---

## 📋 Paso a Paso

### 1️⃣ SETUP DEL BACKEND (15 mins)

```bash
# Abre PowerShell/Terminal 1

# Navega a la carpeta del backend
cd tickets-backend

# Sigue la guía completa:
# DEVELOPMENT_SETUP.md (Backend)
```

**Resumen rápido**:
```bash
# Verifica requisitos
java -version          # Debe ser 21
psql --version        # Debe ser 14+

# Crea BD
psql -U postgres
# Dentro de psql:
CREATE DATABASE tickets_db;
CREATE USER tickets_user WITH PASSWORD 'tickets_password_123';
GRANT ALL PRIVILEGES ON DATABASE tickets_db TO tickets_user;
\q

# Copia el archivo de configuración
# Crea: src/main/resources/application-local.yaml
# Con el contenido de la guía DEVELOPMENT_SETUP.md

# Instala dependencias
./mvnw clean install -DskipTests

# Inicia el Backend
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

**Verifica que funciona**:
```
✅ Ves: "Tomcat started on port(s): 8080"
✅ Puedes acceder: http://localhost:8080/swagger-ui/
```

---

### 2️⃣ SETUP DEL FRONTEND (10 mins)

```bash
# Abre PowerShell/Terminal 2 (NO cierres la Terminal 1)

# Navega a la carpeta del frontend
cd tickets-frontend

# Sigue la guía completa:
# DEVELOPMENT_SETUP.md (Frontend)
```

**Resumen rápido**:
```bash
# Verifica requisitos
node --version        # Debe ser 18+
npm --version        # Debe ser 9+

# Instala dependencias
npm install

# Crea archivo .env
# Contenido:
# VITE_API_BASE_URL=http://localhost:8080/api/v1

# Inicia el Frontend
npm run dev
```

**Verifica que funciona**:
```
✅ Ves: "Local: http://localhost:5173/"
✅ Puedes acceder: http://localhost:5173/
✅ Ves la página de Login
```

---

### 3️⃣ PRUEBA LA CONEXIÓN (10 mins)

Abre http://localhost:5173/ en el navegador:

```bash
# 1. Click en "Registrarse"
# 2. Completa el formulario:
Email: test@example.com
Password: Test123!
Nombres: Juan
Apellidos: Pérez

# 3. Click "Registrarse"
✅ Deberías ser redirigido al Login

# 4. Ingresa credenciales
Email: test@example.com
Password: Test123!

# 5. Click "Iniciar Sesión"
✅ Deberías ver el Dashboard con tu nombre
```

---

## 📁 Estructura de Carpetas Esperada

```
tu-carpeta-proyecto/
├── tickets-backend/
│   ├── src/
│   ├── pom.xml
│   ├── README.md
│   ├── DEVELOPMENT_SETUP.md          ← Lee esto para backend
│   ├── application-local.yaml       ← Crea este archivo
│   └── ...
│
└── tickets-frontend/
    ├── src/
    ├── package.json
    ├── README.md
    ├── DEVELOPMENT_SETUP.md          ← Lee esto para frontend
    ├── .env                          ← Crea este archivo
    └── ...
```

---

## 🔗 Conexión Backend ↔ Frontend

### El Frontend necesita saber dónde está el Backend

**En `tickets-frontend/.env`**:
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

**Cambios según tu setup**:
- Backend en localhost:8080 → `http://localhost:8080/api/v1` ✅
- Backend en otro puerto → Ajusta el puerto
- Backend en otra máquina → Usa la IP de esa máquina

**Ejemplo**:
```env
# Si backend está en máquina 192.168.1.100
VITE_API_BASE_URL=http://192.168.1.100:8080/api/v1
```

---

## ✅ Checklist Final

Antes de decir "está listo", verifica:

- [ ] Backend corre en Terminal 1: `http://localhost:8080/swagger-ui/`
- [ ] Frontend corre en Terminal 2: `http://localhost:5173/`
- [ ] Puedo registrarme desde el frontend
- [ ] Puedo hacer login desde el frontend
- [ ] Veo el Dashboard con mi nombre
- [ ] Puedo crear un ticket
- [ ] No hay errores rojos en la consola (F12)

---

## 🛠️ Troubleshooting Rápido

### "¿No me deja registrar?"
```bash
# Terminal 1 (Backend)
# Verifica que ves:
# [INFO] ... Tomcat started on port(s): 8080

# Si no, siguen faltando pasos en DEVELOPMENT_SETUP.md Backend
```

### "¿Dice 'Cannot connect to backend'?"
```bash
# Verifica que:
# 1. Backend está corriendo
# 2. En Frontend, .env tiene:
#    VITE_API_BASE_URL=http://localhost:8080/api/v1
# 3. Puerto 8080 no está bloqueado por firewall
```

### "¿El login no funciona?"
```bash
# Verifica que los datos son correctos:
# Email: test@example.com
# Password: Test123!

# Si insistes que está bien, limpia localStorage
# En navegador (F12):
localStorage.clear()
location.reload()
```

---

## 📚 Guías Detalladas

Si algo no funciona, accede a las guías completas:

1. **Backend no funciona**: 
   - Lee: `tickets-backend/DEVELOPMENT_SETUP.md`
   - Sección: [Troubleshooting](#troubleshooting)

2. **Frontend no funciona**:
   - Lee: `tickets-frontend/DEVELOPMENT_SETUP.md`
   - Sección: [Troubleshooting](#troubleshooting)

3. **No se conectan**:
   - Backend DEVELOPMENT_SETUP → Paso 8: Verificar
   - Frontend DEVELOPMENT_SETUP → Paso 6: Verificar

---

## 💡 Flujo de Desarrollo Típico

Una vez que todo funciona:

```bash
# Terminal 1: Backend con hot reload (espera cambios)
./mvnw spring-boot:run ...

# Terminal 2: Frontend con hot reload (espera cambios)
npm run dev

# Terminal 3: Control de versiones
git status
git add .
git commit -m "feat: Nueva funcionalidad"
git push
```

**Cada vez que cambias código**:
- Backend: Necesitas reiniciar (Ctrl+C y vuelves a ejecutar)
- Frontend: Se actualiza automáticamente (hot reload)

---

## 🚀 Próximos Pasos

Una vez que funciona localmente:

1. ✅ **Crea ramas para trabajar**:
   ```bash
   git checkout -b feature/tu-funcionalidad
   ```

2. ✅ **Haz cambios y commits frecuentes**:
   ```bash
   git commit -m "feat: Descripción"
   ```

3. ✅ **Sube a GitHub**:
   ```bash
   git push origin feature/tu-funcionalidad
   ```

4. ✅ **Crea Pull Request** en GitHub cuando termines

---

## 📞 ¿Necesitas ayuda?

### Primero, verifica:
1. ¿Realmente seguiste TODOS los pasos?
2. ¿Ejecutaste en el orden correcto?
3. ¿Verificaste con los comandos de verificación?

### Luego, busca en:
1. **Este archivo** (estás aquí)
2. **DEVELOPMENT_SETUP.md Backend** - Para problemas del backend
3. **DEVELOPMENT_SETUP.md Frontend** - Para problemas del frontend
4. **README.md** - Para conceptos generales

### Si aún no funciona:
- Copia el error exacto
- Ve a la sección Troubleshooting que coincida
- Sigue los pasos de solución
- Verifica con los comandos ✅

---

## 🎉 ¡Listo para Desarrollar!

Si llegaste aquí significa que:
- ✅ Backend funciona
- ✅ Frontend funciona
- ✅ Se conectan entre sí
- ✅ Puedes registrarte y hacer login
- ✅ El sistema completo está operativo

**¡Ahora puedes empezar a hacer cambios y mejoras!**

---

**Guía para entregar**: Comparte este archivo + `DEVELOPMENT_SETUP.md` de ambos proyectos

