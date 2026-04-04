# Documento de High Level Design - Arquitectura de Backend
## Sistema de Gestión de Biblioteca - DOSW Library

**Proyecto:** DOSW-Library API  
**Versión:** 1.0  
**Fecha:** Abril 2026  
**Autor:** DOSW Company

---

## 1. Descripción General

El sistema DOSW Library es una API RESTful desarrollada con Spring Boot que gestiona las operaciones de una biblioteca digital. El sistema soporta persistencia dual (relacional con PostgreSQL y no relacional con MongoDB Atlas), autenticación JWT y despliegue en la nube mediante Azure App Service.

---

## 2. Arquitectura General

El sistema sigue una arquitectura de capas (Layered Architecture) organizada en los siguientes niveles:

```
┌─────────────────────────────────────────┐
│           CLIENTE / SWAGGER UI           │
└────────────────┬────────────────────────┘
                 │ HTTP / REST
┌────────────────▼────────────────────────┐
│           CAPA DE CONTROLADORES          │
│     BookController | UserController      │
│          LoanController                  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         CAPA DE SEGURIDAD (JWT)          │
│    JwtAuthFilter | SecurityConfig        │
│         JwtService | AuthService         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│           CAPA DE SERVICIOS              │
│   BookService | UserService | LoanService│
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         CAPA DE PERSISTENCIA             │
│  ┌──────────────┐  ┌──────────────────┐  │
│  │  RELACIONAL  │  │  NO RELACIONAL   │  │
│  │  (JPA/Hibern)│  │   (MongoDB)      │  │
│  │  repository/ │  │  mongorepository/│  │
│  │  entity/     │  │  mongodocument/  │  │
│  │  mapper/     │  │  mapper/         │  │
│  └──────┬───────┘  └────────┬─────────┘  │
└─────────┼───────────────────┼────────────┘
          │                   │
┌─────────▼───────┐  ┌────────▼──────────┐
│   PostgreSQL    │  │   MongoDB Atlas   │
│  (Nube/Local)   │  │   (Cluster0)      │
└─────────────────┘  └───────────────────┘
```

---

## 3. Componentes Principales

### 3.1 Capa de Controladores
Expone los endpoints REST de la API. Cada controlador maneja las peticiones HTTP y delega la lógica a los servicios.

| Controlador | Endpoints principales |
|-------------|----------------------|
| BookController | GET/POST /books, DELETE /books/{id}, PUT /books/{id}/availability |
| UserController | GET/POST /users, DELETE /users/{id}, PUT /users/{id} |
| LoanController | GET/POST /loans, PUT /loans/{id}/return |
| AuthController | POST /auth/login |

### 3.2 Capa de Seguridad
Implementada con Spring Security y JWT (JSON Web Tokens).

- **JwtService:** Genera y valida tokens JWT con claims de userId y role.
- **JwtAuthFilter:** Filtro que intercepta cada petición y valida el token.
- **SecurityConfig:** Define las reglas de acceso por rol (LIBRARIAN / USER).

| Rol | Permisos |
|-----|----------|
| LIBRARIAN | Crear, editar, eliminar libros y usuarios. Gestionar préstamos. |
| USER | Consultar libros y usuarios. Crear préstamos. |

### 3.3 Capa de Servicios
Contiene la lógica de negocio del sistema. Los servicios son independientes de la tecnología de persistencia gracias al uso de interfaces de repositorio.

### 3.4 Capa de Persistencia Dual

#### Persistencia Relacional (JPA + Hibernate + PostgreSQL)
- **Entidades:** BookEntity, UserEntity, LoanEntity, LoanHistoryEntity
- **Repositorios:** BookRepository, UserRepository, LoanRepository (extienden JpaRepository)
- **Mappers:** Convierten entre entidades JPA y modelos de dominio

#### Persistencia No Relacional (MongoDB Atlas)
- **Documentos:** BookDocument, UserDocument, LoanDocument
- **Repositorios:** BookMongoRepository, UserMongoRepository, LoanMongoRepository (extienden MongoRepository)
- **Mappers:** Convierten entre documentos MongoDB y modelos de dominio

---

## 4. Modelo de Datos

### 4.1 Modelo Relacional (PostgreSQL)

```
┌──────────────┐       ┌──────────────┐       ┌──────────────────┐
│    books     │       │    loans     │       │   loan_history   │
├──────────────┤       ├──────────────┤       ├──────────────────┤
│ book_id (PK) │◄──────│ book_id (FK) │       │ id (PK)          │
│ title        │       │ loan_id (PK) │◄──────│ loan_id (FK)     │
│ author       │       │ user_id (FK) │       │ status           │
│ isbn         │       │ loan_date    │       │ date             │
│ status       │       │ return_date  │       └──────────────────┘
│ total_copies │       │ status       │
│ ...          │       └──────────────┘
└──────────────┘              │
                              │
                    ┌─────────▼────┐
                    │    users     │
                    ├──────────────┤
                    │ user_id (PK) │
                    │ username     │
                    │ password     │
                    │ role         │
                    │ email        │
                    │ membershipType│
                    └──────────────┘
```

### 4.2 Modelo No Relacional (MongoDB Atlas)

```
Collection: books
{
  _id, title, author, isbn, publicationType,
  publicationDate, status, totalCopies,
  availableCopies, borrowedCopies,
  categories: [...],
  metadata: { pages, language, publisher },
  availability: { status, totalCopies, ... },
  addedToCatalogDate
}

Collection: users
{
  _id, username, email, role,
  membershipType, registeredDate
}

Collection: loans
{
  _id, bookId, userId,
  loanDate, returnDate, status,
  history: [{ status, date }, ...]
}
```

---

## 5. Pipeline CI/CD

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  BUILD   │───►│   TEST   │───►│ANALYSIS  │    │  DEPLOY  │
│          │    │          │    │          │    │          │
│ mvn      │    │ mvn test │    │checkstyle│    │ Azure    │
│ compile  │    │ (service │    │jacoco    │    │ App Svc  │
│          │    │  tests)  │    │          │    │          │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
                                                      ▲
                                                      │ needs: test
```

**Trigger:** Pull Request hacia la rama `main`

**Jobs:**
1. **Build:** Compila el proyecto con `mvn compile`
2. **Test:** Ejecuta los tests de servicio con `mvn test`
3. **Analysis:** Análisis estático con Checkstyle y reporte de cobertura con JaCoCo
4. **Deploy:** Empaqueta el JAR y lo despliega en Azure App Service

---

## 6. Infraestructura en la Nube

```
┌─────────────────────────────────────────────────┐
│                   AZURE CLOUD                    │
│                                                  │
│  ┌─────────────────────────────────────────┐    │
│  │         Azure App Service (F1)           │    │
│  │         dosw-library-api                 │    │
│  │         Canada Central                   │    │
│  │         Java 21 SE / Linux               │    │
│  └──────────────────┬──────────────────────┘    │
│                     │                            │
└─────────────────────┼────────────────────────────┘
                      │
          ┌───────────▼──────────┐
          │    MongoDB Atlas     │
          │    AWS / US-EAST-1   │
          │    Cluster0 (M0)     │
          └──────────────────────┘
```

**Variables de entorno en Azure App Service:**
- `SPRING_DATA_MONGODB_URI`: URI de conexión a MongoDB Atlas
- `PORT`: Puerto de la aplicación (80)

---

## 7. Tecnologías Utilizadas

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.3.2 |
| Persistencia Relacional | Spring Data JPA + Hibernate + PostgreSQL |
| Persistencia No Relacional | Spring Data MongoDB + MongoDB Atlas |
| Seguridad | Spring Security + JWT (jjwt 0.11.5) |
| Documentación API | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| CI/CD | GitHub Actions |
| Nube | Microsoft Azure App Service |
| Contenedor | Linux (Ubuntu) |

---

## 8. Flujo de Autenticación

```
Cliente          AuthController       AuthService         JwtService
   │                   │                   │                   │
   │── POST /auth/login─►│                   │                   │
   │                   │── authenticate() ──►│                   │
   │                   │                   │── findByUsername()  │
   │                   │                   │── validatePassword()│
   │                   │                   │── generateToken() ──►│
   │                   │                   │◄── JWT Token ───────│
   │◄── JWT Token ─────│◄── JWT Token ─────│                   │
   │                   │                   │                   │
   │── GET /books (Bearer JWT) ────────────────────────────────►│
   │                   │◄── validateToken()──────────────────────│
   │◄── 200 OK ────────│                   │                   │
```
