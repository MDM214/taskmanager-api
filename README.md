## Portafolio profesional
 
TaskManager API forma parte de mi portafolio profesional, donde se presenta el proyecto junto con su documentación, capturas, pruebas automatizadas y decisiones técnicas.
 
🌐 https://portafolio-manuel-setien.vercel.app/

# TaskManager API

API REST para la gestión de usuarios y tareas, desarrollada con **Spring Boot**, autenticación **JWT**, persistencia con **Spring Data JPA** y una batería completa de pruebas automatizadas.

El proyecto forma parte de un portafolio profesional de Desarrollo de Aplicaciones Multiplataforma y muestra una arquitectura backend por capas, seguridad sin estado, validación de datos, documentación OpenAPI y control de calidad con JUnit, Mockito, MockMvc y JaCoCo.

## Estado del proyecto

- Aplicación funcional con MariaDB.
- Autenticación mediante email y contraseña.
- Autorización mediante token JWT.
- CRUD completo de usuarios y tareas.
- Validación de peticiones y respuestas de error uniformes.
- Documentación interactiva con Swagger UI.
- Perfil de pruebas independiente con H2 en memoria.
- **57 tests automatizados superados**.
- **97,03 % de cobertura global de líneas**.
- Build verificado con `mvnw clean verify`.

## Funcionalidades principales

### Usuarios

- Listar usuarios.
- Consultar un usuario por identificador.
- Crear usuarios.
- Actualizar nombre y correo electrónico.
- Eliminar usuarios.
- Codificar contraseñas con BCrypt.
- Asignar el rol `USER` durante el registro.
- Evitar que la contraseña y el rol aparezcan en las respuestas públicas.

### Tareas

- Listar tareas.
- Consultar una tarea por identificador.
- Crear tareas asociadas a usuarios.
- Actualizar título, descripción y estado.
- Eliminar tareas.
- Validar los datos de entrada.

### Autenticación y seguridad

- Inicio de sesión mediante email y contraseña.
- Generación de tokens JWT.
- Validación de firma y expiración del token.
- Seguridad sin sesiones de servidor.
- Endpoints públicos y protegidos.
- Recuperación del usuario autenticado mediante `/auth/whoami`.

## Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
|---|---|
| Java 24 | Lenguaje principal |
| Spring Boot 4.1.1 | Base de la aplicación |
| Spring MVC | API REST |
| Spring Data JPA | Persistencia y repositorios |
| Hibernate ORM | Mapeo objeto-relacional |
| Spring Security | Autenticación y autorización |
| JJWT 0.12.7 | Creación y validación de JWT |
| MariaDB Connector/J 3.5.9 | Conexión con MariaDB |
| MariaDB | Base de datos de desarrollo y ejecución |
| H2 | Base de datos en memoria para pruebas |
| Jakarta Validation | Validación de DTO y entidades |
| Springdoc OpenAPI | Documentación Swagger |
| JUnit Jupiter | Pruebas automatizadas |
| Mockito | Simulación de dependencias |
| MockMvc | Pruebas de controladores y seguridad |
| JaCoCo 0.8.15 | Informe de cobertura |
| Maven Wrapper | Compilación y gestión de dependencias |

## Arquitectura

El proyecto sigue una arquitectura por capas:

```text
Cliente HTTP
    |
    v
SecurityFilterChain
    |
    v
JwtAuthenticationFilter
    |
    v
Controllers
    |
    v
Services
    |
    v
Repositories
    |
    v
MariaDB
```

### Responsabilidad de cada capa

- **Controller:** recibe peticiones HTTP, valida datos y devuelve DTO.
- **Service:** contiene las reglas de negocio.
- **Repository:** gestiona el acceso a datos mediante Spring Data JPA.
- **Entity:** representa las tablas y relaciones persistentes.
- **DTO:** define contratos de entrada y salida.
- **Security:** valida JWT y configura las rutas públicas y privadas.
- **Exception:** centraliza los errores y sus códigos HTTP.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/com/manuel/taskmanager/
│   │   ├── config/
│   │   │   ├── OpenApiConfig.java
│   │   │   ├── PasswordConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── TareaController.java
│   │   │   └── UsuarioController.java
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── service/
│   │   └── TaskmanagerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/manuel/taskmanager/
    │   ├── controller/
    │   ├── security/
    │   ├── service/
    │   └── TaskmanagerApplicationTests.java
    └── resources/
        └── application-test.properties
```

## Modelo de datos

### Usuario

| Campo | Tipo | Reglas principales |
|---|---|---|
| `id` | Long | Clave primaria autogenerada |
| `nombre` | String | Obligatorio |
| `email` | String | Obligatorio y formato válido |
| `password` | String | Se almacena codificada |
| `rol` | String | `USER` por defecto |
| `tareas` | List<Tarea> | Relación uno a muchos |

### Tarea

| Campo | Tipo | Reglas principales |
|---|---|---|
| `id` | Long | Clave primaria autogenerada |
| `titulo` | String | Obligatorio, entre 2 y 100 caracteres |
| `descripcion` | String | Máximo 500 caracteres |
| `estado` | String | Estado de la tarea |
| `usuario` | Usuario | Relación muchos a uno |

### Relación

```text
Usuario 1 ---------------- N Tarea
usuarios.id <------------- tareas.usuario_id
```

## Requisitos

Para ejecutar la aplicación se necesita:

- JDK 24.
- MariaDB 10.6 o superior recomendada.
- Puerto `8080` disponible.
- Puerto `3306` disponible para MariaDB, salvo que se configure otro.
- Git.

No es necesario instalar Maven globalmente porque el proyecto incluye Maven Wrapper.

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/MDM214/taskmanager-api.git
cd taskmanager
```

### 2. Crear la base de datos

```sql
CREATE DATABASE taskmanager
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

Se recomienda crear un usuario específico para la aplicación y concederle permisos únicamente sobre esta base de datos.

### 3. Configurar las variables de entorno

El archivo `.env.example` documenta las variables necesarias:

```dotenv
DB_URL=jdbc:mariadb://localhost:3306/taskmanager
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

JWT_SECRET=escribe-una-clave-aleatoria-de-al-menos-32-caracteres
JWT_EXPIRATION=3600000
```

> El archivo `.env.example` es únicamente una plantilla. Spring Boot obtiene los valores reales desde las variables del sistema operativo o del entorno de despliegue.

### Variables obligatorias

- `DB_PASSWORD`
- `JWT_SECRET`

### Variables con valores predeterminados

- `DB_URL`: `jdbc:mariadb://localhost:3306/taskmanager`
- `DB_USERNAME`: `root`
- `JPA_DDL_AUTO`: `update`
- `JPA_SHOW_SQL`: `false`
- `JWT_EXPIRATION`: `3600000` milisegundos

Para un entorno profesional se recomienda configurar explícitamente todas las variables y evitar el usuario `root`.

## Ejecución

### Ejecutar en desarrollo

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La API estará disponible en:

```text
http://localhost:8080
```

### Generar el JAR

```powershell
.\mvnw.cmd clean verify
```

El artefacto se genera en:

```text
target/taskmanager-0.0.1-SNAPSHOT.jar
```

### Ejecutar el JAR

```powershell
java -jar .\target\taskmanager-0.0.1-SNAPSHOT.jar
```

## Swagger y OpenAPI

Con la aplicación iniciada:

```text
Swagger UI: http://localhost:8080/swagger-ui/index.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

Las rutas de Swagger y OpenAPI son públicas.

## Autenticación JWT

### 1. Iniciar sesión

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "email": "usuario@example.com",
  "password": "password123"
}
```

Respuesta correcta:

```json
{
  "token": "eyJ..."
}
```

### 2. Usar el token

```http
Authorization: Bearer eyJ...
```

Ejemplo con PowerShell:

```powershell
Invoke-WebRequest "http://localhost:8080/tareas" `
    -Headers @{ Authorization = "Bearer TU_TOKEN" }
```

## Endpoints

### Autenticación

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/auth/login` | Público | Autentica y devuelve un JWT |
| GET | `/auth/whoami` | Bajo patrón público `/auth/**` | Devuelve el nombre del usuario autenticado cuando existe autenticación |

### Usuarios

| Método | Ruta | Descripción | Respuesta correcta |
|---|---|---|---|
| GET | `/usuarios` | Lista todos los usuarios | 200 |
| GET | `/usuarios/{id}` | Obtiene un usuario | 200 |
| POST | `/usuarios` | Crea un usuario | 200 |
| PUT | `/usuarios/{id}` | Actualiza un usuario | 200 |
| DELETE | `/usuarios/{id}` | Elimina un usuario | 204 |

### Tareas

| Método | Ruta | Descripción | Respuesta correcta |
|---|---|---|---|
| GET | `/tareas` | Lista todas las tareas | 200 |
| GET | `/tareas/{id}` | Obtiene una tarea | 200 |
| POST | `/tareas` | Crea una tarea | 200 |
| PUT | `/tareas/{id}` | Actualiza una tarea | 200 |
| DELETE | `/tareas/{id}` | Elimina una tarea | 204 |

Todos los endpoints de usuarios y tareas requieren un JWT válido.

## Códigos de error

| Situación | Código HTTP |
|---|---|
| Datos de entrada no válidos | 400 Bad Request |
| Credenciales inválidas | 401 Unauthorized |
| Acceso a ruta protegida sin autenticación | 403 Forbidden |
| Recurso inexistente | 404 Not Found |
| Eliminación correcta | 204 No Content |

Ejemplo de error:

```json
{
  "mensaje": "Tarea no encontrada con ID: 99"
}
```

## Pruebas automatizadas

La suite se ejecuta con:

```powershell
.\mvnw.cmd clean test
```

La verificación completa, incluida la cobertura, se ejecuta con:

```powershell
.\mvnw.cmd clean verify
```

### Distribución de pruebas

| Clase de prueba | Número |
|---|---:|
| `TaskmanagerApplicationTests` | 1 |
| `TareaServiceTest` | 8 |
| `UsuarioServiceTest` | 8 |
| `CustomUserDetailsServiceTest` | 2 |
| `JwtServiceTest` | 5 |
| `TareaControllerTest` | 9 |
| `UsuarioControllerTest` | 9 |
| `AuthControllerTest` | 5 |
| `JwtAuthenticationFilterTest` | 5 |
| `SecurityIntegrationTest` | 5 |
| **Total** | **57** |

Resultado validado:

```text
Tests run: 57
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Entorno de pruebas reproducible

Las pruebas de contexto utilizan el perfil `test` y una base H2 temporal en memoria:

```text
jdbc:h2:mem:taskmanager_test
```

Esto permite ejecutar la suite sin tener MariaDB iniciada y sin utilizar credenciales reales.

## Cobertura

JaCoCo genera el informe durante la fase `verify`.

```powershell
.\mvnw.cmd clean verify
```

Informe HTML:

```text
target/site/jacoco/index.html
```

Resultados actuales:

- **24 clases analizadas**.
- **294 líneas cubiertas**.
- **9 líneas no cubiertas**.
- **97,03 % de cobertura global de líneas**.
- Services, controllers y seguridad principal con 100 % de líneas cubiertas.

## Decisiones técnicas destacadas

- Uso de DTO para no exponer entidades completas.
- Contraseñas codificadas con BCrypt.
- JWT y sesiones `STATELESS`.
- Validación declarativa con Jakarta Validation.
- Manejo centralizado de excepciones.
- Tests de Services sin base de datos real.
- Tests MVC separados de los tests integrados de seguridad.
- H2 para lograr una suite reproducible.
- MariaDB Connector/J 3.5.9 para evitar la exposición de credenciales en los metadatos JDBC observada en una versión posterior.

## Limitaciones conocidas

- El entorno local utilizado durante el desarrollo emplea MariaDB 10.4.32 de XAMPP. Para un despliegue se recomienda MariaDB 10.6 o superior.
- Los endpoints `POST` devuelven actualmente `200 OK`. Una mejora futura puede adoptar `201 Created` y cabecera `Location`.
- El filtro JWT crea una autenticación sin autoridades.
- No existen refresh tokens ni revocación de tokens.
- Java 24 muestra una advertencia sobre la carga dinámica del agente de Mockito.
- `ddl-auto=update` es apropiado para desarrollo, pero se recomiendan migraciones con Flyway o Liquibase en producción.

## Mejoras futuras

- Docker y Docker Compose.
- Pipeline de integración continua.
- Despliegue público.
- MariaDB moderna en el entorno de ejecución.
- Migraciones de base de datos.
- Refresh tokens.
- Roles y permisos adicionales.
- Paginación y filtros.
- Búsquedas de tareas.
- Recuperación de contraseña.
- `201 Created` en operaciones de creación.
- Umbral automático mediante `jacoco:check`.

## Documentación complementaria

El proyecto dispone además de:

- documentación técnica completa;
- guía reproducible de las sesiones 1 a 9;
- cuaderno de bitácora;
- Swagger UI;
- informe JaCoCo.

## Seguridad

- No publiques `DB_PASSWORD` ni `JWT_SECRET`.
- No añadas archivos `.env` reales al repositorio.
- Utiliza secretos diferentes por entorno.
- Usa HTTPS en un despliegue público.
- Utiliza un usuario de base de datos con permisos mínimos.
- Rota cualquier credencial que aparezca accidentalmente en un log o captura.

## Autor

**Manuel Setién Joya**  
Desarrollo de Aplicaciones Multiplataforma, DAM  
Proyecto de portafolio profesional


## Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulta el archivo `LICENSE` para más información.
