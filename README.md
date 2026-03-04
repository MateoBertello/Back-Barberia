# ✂️ BarberSaaS — Sistema de Reservas para Barberías

> Plataforma web full-stack para la gestión integral de barberías: turnos, staff, servicios y sucursales.

🚧 **En desarrollo activo**

---

## 🛠️ Stack tecnológico

### Frontend
| Tecnología | Uso |
|---|---|
| React 18 + TypeScript | UI principal |
| Vite | Bundler y dev server |
| React Router v7 | Navegación SPA con rutas protegidas |
| Tailwind CSS v4 | Estilos utilitarios |
| shadcn/ui + Radix UI | Componentes accesibles |
| Sonner | Sistema de notificaciones (toasts) |

### Backend
| Tecnología | Uso |
|---|---|
| Spring Boot 4 (Java 17) | API REST |
| Spring Security + JWT (JJWT) | Autenticación stateless |
| Spring Data JPA + Hibernate | Persistencia ORM |
| PostgreSQL (Supabase) | Base de datos en la nube |
| BCrypt | Encriptación de contraseñas |

---

## 🧩 Funcionalidades por rol

### Cliente
- Registro e inicio de sesión
- Wizard de reserva paso a paso: sucursal → servicio → barbero → horario → confirmación
- Historial de turnos con estado, precio y barbero asignado

### Barbero
- Agenda diaria filtrada por usuario autenticado
- Cambio de estado de turnos: Pendiente → Completado / Cancelado

### Dueño / Admin
- Dashboard con todos los turnos del sistema
- CRUD de Staff (barberos)
- CRUD de Servicios (nombre, duración, precio)
- CRUD de Sucursales (nombre, dirección, teléfono)
- Configuración de horarios *(próximamente)*

---

## 🔐 Seguridad

- Contraseñas almacenadas con **BCrypt**
- Autenticación via **JWT Bearer Token** con validez de 24 horas
- Filtro `JwtFilter` intercepta cada request y valida el token antes de procesarlo
- Rutas públicas: solo `/api/auth/**` — el resto requiere token válido
- Token expirado: redirige automáticamente al login y limpia el storage

| Rol | Ruta frontend |
|---|---|
| CLIENTE | `/client` |
| BARBERO | `/barber` |
| DUEÑO | `/admin` |

---

## 📁 Estructura del proyecto

### Frontend

```
src/
  app/
    components/
      admin/       → Dashboard, Staff, Servicios, Sucursales, Horarios
      barber/      → Layout y Dashboard del barbero
      client/      → Layout, Dashboard y Wizard de reserva
      auth/        → ProtectedRoute con control de roles
      login/       → LoginPage (login + registro)
      ui/          → Componentes shadcn/ui
      utils/
        apsClient.ts  → HTTP client centralizado (JWT + error toasts)
    constants.ts   → Tokens de diseño (colores, tipografías)
    routes.tsx     → Rutas protegidas por rol
  styles/
    theme.css      → Variables CSS personalizadas
    fonts.css      → Google Fonts
```

### Backend

```
src/main/java/com/example/bareberiaapi/
  controller/     → AuthController, TurnoController, UsuarioController...
  service/        → Lógica de negocio por entidad
  repository/     → Interfaces JpaRepository
  entity/         → Usuario, Turno, Servicio, Sucursal, HorarioBarbero
  security/       → JwtUtil, JwtFilter, SecurityConfig
  config/         → CorsConfig, GlobalExceptionHandler
  dto/            → AuthResponse, LoginRequest
```

---

## 🗄️ Modelo de datos

```
Usuario        (id, nombre, email, contrasena, telefono, rol, activo)
               rol: DUEÑO | BARBERO | CLIENTE

Turno          (id, cliente→Usuario, barbero→Usuario, servicio→Servicio,
                sucursal→Sucursal, fechaHoraInicio, estado)
               estado: PENDIENTE | COMPLETADO | CANCELADO

Servicio       (id, nombre, duracionMinutos, precio, activo)

Sucursal       (id, nombre, direccion, telefono, activa)

HorarioBarbero (id, barbero→Usuario, sucursal→Sucursal,
                diaSemana, horaInicio, horaFin)
```

---

## ⚙️ Instalación y ejecución

### Prerrequisitos
- Node.js 18+ (frontend)
- Java 17+ y Maven (backend)

### Frontend

```bash
cd frontend
npm install
npm run dev
# Corre en http://localhost:5173
```

Variable de entorno opcional (crear archivo `.env`):

```
VITE_API_URL=http://localhost:8080/api
```

Si no se define, apunta a `http://localhost:8080/api` por defecto.

### Backend

```bash
cd backend
./mvnw spring-boot:run
# API disponible en http://localhost:8080
```

La base de datos está en **Supabase (PostgreSQL)**. Hibernate crea y actualiza las tablas automáticamente con `ddl-auto=update`.

---

## 🌐 Endpoints principales de la API

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión | No |
| POST | `/api/auth/register` | Registrar cliente | No |
| GET | `/api/turnos` | Listar todos los turnos | JWT |
| POST | `/api/turnos` | Crear turno | JWT |
| PUT | `/api/turnos/{id}/estado` | Cambiar estado del turno | JWT |
| GET | `/api/usuarios` | Listar usuarios | JWT |
| GET | `/api/servicios` | Listar servicios | JWT |
| POST | `/api/servicios` | Crear servicio | JWT |
| GET | `/api/sucursales` | Listar sucursales | JWT |
| POST | `/api/sucursales` | Crear sucursal | JWT |

---
