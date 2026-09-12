# TaskManager API

API REST desarrollada con Spring Boot y MariaDB para la gestión de usuarios y tareas.

## Tecnologías

- Java 24
- Spring Boot
- Maven
- MariaDB
- JPA / Hibernate
- Git
- GitHub

## Arquitectura

Controller
↓
Service
↓
Repository
↓
Database

## Funcionalidades

### Usuarios

- Crear usuario
- Obtener usuarios
- Obtener usuario por ID
- Actualizar usuario
- Eliminar usuario

### Tareas

- Crear tarea
- Obtener tareas
- Obtener tarea por ID
- Actualizar tarea
- Eliminar tarea

## Endpoints

### Usuarios

```http
GET /usuarios
GET /usuarios/{id}
POST /usuarios
PUT /usuarios/{id}
DELETE /usuarios/{id}