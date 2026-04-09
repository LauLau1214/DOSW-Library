# DOSW Library API - Guía Completa

Sistema de gestión de biblioteca desarrollado con Spring Boot, con persistencia dual
(PostgreSQL + MongoDB Atlas), seguridad JWT y despliegue en Azure App Service
mediante CI/CD con GitHub Actions.

---

# ÍNDICE

1. [Evolución del proyecto](#evolución-del-proyecto)
2. [Diagramas de Componentes](#diagramas-de-componentes)
3. [Diagrama Entidad-Relación](#diagrama-entidad-relación)
4. [Diagrama de Clases](#diagrama-de-clases)
5. [Patrones de Diseño identificados](#patrones-de-diseño)
6. [Persistencia Dual JPA + MongoDB](#persistencia-dual)
7. [Seguridad JWT](#seguridad-jwt)
8. [CI/CD con GitHub Actions](#cicd)
9. [Deploy en Azure](#deploy-en-azure)
10. [Tests](#tests)
11. [Tecnologías](#tecnologías)
12. [Anexos](#anexos)

---

# EVOLUCIÓN DEL PROYECTO

## Fase 1 - En memoria (sin persistencia)
El sistema inicial manejaba los datos en listas y mapas en memoria:
```java
// Así se veía antes
private List<User> users = new ArrayList<>();
private Map<String, Integer> books = new HashMap<>(); // libro → cantidad
private List<Loan> loans = new ArrayList<>();
```
**Problema:** Al reiniciar la app se perdían todos los datos.

## Fase 2 - Persistencia Relacional (JPA + PostgreSQL)
Se agregó base de datos real. Los datos ahora sobreviven reinicios.
- Se crearon entidades JPA (`@Entity`)
- Se crearon repositorios (`JpaRepository`)
- Se configuró PostgreSQL en `application.properties`

## Fase 3 - Seguridad JWT
Se agregó autenticación y autorización:
- Roles: `LIBRARIAN` y `USER`
- Token JWT para cada sesión
- Filtro que valida el token en cada petición

## Fase 4 - Persistencia NoSQL (MongoDB Atlas)
Se agregó MongoDB para tener persistencia dual:
- Datos relacionales en PostgreSQL
- Datos flexibles/escalables en MongoDB

## Fase 5 - CI/CD y Deploy en Azure
- Pipeline automático con GitHub Actions
- Deploy en Azure App Service

---

# DIAGRAMAS DE COMPONENTES

## ¿Qué es un diagrama de componentes?
Un diagrama de componentes muestra cómo están organizadas las partes
del sistema y cómo se comunican entre sí. Hay dos niveles:

- **General (de alto nivel):** Muestra el sistema completo y sus grandes bloques
- **Específico (de bajo nivel):** Muestra el detalle interno de cada capa

---

## Diagrama General (Alto Nivel)

Este diagrama muestra el sistema completo visto desde afuera:

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE                               │
│              (Swagger UI / Postman)                      │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP/HTTPS
                         │ Authorization: Bearer <JWT>
┌────────────────────────▼────────────────────────────────┐
│                  DOSW LIBRARY API                        │
│             (Azure App Service - Java 21)                │
│                                                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │              Spring Boot Application              │   │
│  └──────────────────────────────────────────────────┘   │
└──────────────┬──────────────────────┬───────────────────┘
               │                      │
┌──────────────▼──────┐   ┌───────────▼──────────────────┐
│    PostgreSQL        │   │       MongoDB Atlas           │
│  (Base relacional)   │   │    (Base no relacional)       │
│  - books             │   │    - books                   │
│  - users             │   │    - users                   │
│  - loans             │   │    - loans                   │
│  - loan_history      │   │                              │
└─────────────────────┘   └──────────────────────────────┘
```

**¿Cómo leerlo?**
- El cliente se comunica con la API via HTTP
- La API tiene dos bases de datos al mismo tiempo
- PostgreSQL para datos estructurados
- MongoDB para datos flexibles

---

## Diagrama Específico (Bajo Nivel)

Este diagrama muestra el detalle interno de la aplicación:

```
┌────────────────────────────────────────────────────────────────┐
│                     SPRING BOOT APPLICATION                     │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                  CAPA DE SEGURIDAD                       │   │
│  │   JwtAuthFilter → JwtService → SecurityConfig           │   │
│  │   AuthController → AuthService                          │   │
│  └───────────────────────────┬─────────────────────────────┘   │
│                              │                                   │
│  ┌───────────────────────────▼─────────────────────────────┐   │
│  │                CAPA DE CONTROLADORES                     │   │
│  │                                                          │   │
│  │  BookController  UserController  LoanController          │   │
│  │       │               │               │                  │   │
│  │  BookDTO         UserDTO         LoanDTO                 │   │
│  │  BookMapper      UserMapper      LoanMapper              │   │
│  └───────────────────────────┬─────────────────────────────┘   │
│                              │                                   │
│  ┌───────────────────────────▼─────────────────────────────┐   │
│  │                  CAPA DE SERVICIOS                       │   │
│  │                                                          │   │
│  │    BookService      UserService      LoanService         │   │
│  │        │                │                │               │   │
│  │    Book(model)     User(model)      Loan(model)          │   │
│  └───────────────────────────┬─────────────────────────────┘   │
│                              │                                   │
│  ┌───────────────────────────▼─────────────────────────────┐   │
│  │                CAPA DE PERSISTENCIA                      │   │
│  │                                                          │   │
│  │  ┌──────────────────────┐  ┌───────────────────────┐    │   │
│  │  │     RELACIONAL       │  │    NO RELACIONAL       │    │   │
│  │  │                      │  │                        │    │   │
│  │  │ BookEntity           │  │ BookDocument           │    │   │
│  │  │ UserEntity           │  │ UserDocument           │    │   │
│  │  │ LoanEntity           │  │ LoanDocument           │    │   │
│  │  │ LoanHistoryEntity    │  │                        │    │   │
│  │  │                      │  │                        │    │   │
│  │  │ BookRepository       │  │ BookMongoRepository    │    │   │
│  │  │ UserRepository       │  │ UserMongoRepository    │    │   │
│  │  │ LoanRepository       │  │ LoanMongoRepository    │    │   │
│  │  │                      │  │                        │    │   │
│  │  │ BookPersistMapper    │  │ BookDocumentMapper     │    │   │
│  │  └──────────┬───────────┘  └──────────┬────────────┘    │   │
│  └─────────────┼──────────────────────────┼─────────────────┘   │
└────────────────┼──────────────────────────┼─────────────────────┘
                 │                          │
     ┌───────────▼──────┐       ┌───────────▼──────┐
     │   PostgreSQL      │       │  MongoDB Atlas    │
     └──────────────────┘       └──────────────────┘
```

**¿Cómo leerlo?**
Cada capa solo habla con la capa inmediatamente debajo:
- Controller solo llama a Service
- Service solo llama a Repository
- Repository habla con la base de datos

---

## TIPS PARA HACER DIAGRAMAS DE COMPONENTES EN EL PARCIAL

### Diagrama General - Identifica estos bloques:
1. **¿Quién consume la API?** → El cliente (Postman, Swagger, Frontend)
2. **¿Dónde corre la app?** → El servidor (Azure, local, etc.)
3. **¿Qué bases de datos usa?** → PostgreSQL, MongoDB, H2, etc.
4. **¿Hay servicios externos?** → OAuth, email, etc.

### Diagrama Específico - Identifica estas capas:
1. **Seguridad** → Filtros JWT, configuración de Spring Security
2. **Controllers** → Endpoints + DTOs + Mappers de entrada/salida
3. **Services** → Lógica de negocio + modelos de dominio
4. **Persistence** → Entidades + Repositorios + Mappers de persistencia
5. **Base de datos** → Las tablas/colecciones

### Regla de oro:
> Cada componente solo conoce al componente de la capa inmediatamente debajo.
> Un Controller NUNCA habla directamente con un Repository.

---

# DIAGRAMA ENTIDAD-RELACIÓN

## ¿Qué es y para qué sirve?
El diagrama ER muestra las tablas de la base de datos relacional,
sus campos y cómo se relacionan entre sí.

## Diagrama ER del Sistema

```
┌─────────────────┐         ┌──────────────────────┐
│     BOOKS        │         │       LOANS           │
├─────────────────┤         ├──────────────────────┤
│ book_id (PK)    │◄────────│ book_id (FK)          │
│ title           │  1    N │ loan_id (PK)          │
│ author          │         │ user_id (FK)          │
│ isbn            │         │ loan_date             │
│ publication_type│         │ return_date           │
│ publication_date│         │ status                │
│ status          │         └──────────┬───────────┘
│ total_copies    │                    │ 1
│ available_copies│                    │
│ borrowed_copies │                    │ N
│ language        │         ┌──────────▼───────────┐
│ pages           │         │    LOAN_HISTORY       │
│ publisher       │         ├──────────────────────┤
│ added_to_catalog│         │ id (PK)              │
└─────────────────┘         │ loan_id (FK)          │
                            │ status                │
┌─────────────────┐         │ date                  │
│     USERS        │         └──────────────────────┘
├─────────────────┤
│ user_id (PK)    │◄────────( loans.user_id )
│ username        │  1    N
│ password        │
│ role            │
│ email           │
│ membership_type │
│ registered_date │
└─────────────────┘

┌──────────────────────┐
│   BOOK_CATEGORIES     │
├──────────────────────┤
│ book_id (FK)          │
│ category              │
└──────────────────────┘
```

## Relaciones:
- Un **BOOK** puede tener muchos **LOANS** (1:N)
- Un **USER** puede tener muchos **LOANS** (1:N)
- Un **LOAN** puede tener muchos **LOAN_HISTORY** (1:N)
- Un **BOOK** puede tener muchas **CATEGORIES** (1:N)

## Normalización 3FN:
- **1FN:** Todos los campos son atómicos (un valor por celda)
- **2FN:** Cada campo depende de toda la clave primaria
- **3FN:** No hay dependencias transitivas (ej: publisher no depende de author)

## TIPS PARA HACER EL DIAGRAMA ER EN EL PARCIAL

1. **Identifica las entidades** → ¿De qué "cosas" habla el enunciado?
   - Sustantivos importantes = entidades (Libro, Usuario, Préstamo, Producto, etc.)

2. **Identifica los atributos** → ¿Qué datos tiene cada entidad?
   - Lee el enunciado y busca características de cada entidad

3. **Identifica las relaciones** → ¿Cómo se conectan?
   - "Un usuario PUEDE TENER muchos préstamos" → 1:N
   - "Un préstamo pertenece a UN usuario" → N:1
   - "Un libro puede estar en muchas categorías y una categoría en muchos libros" → N:M

4. **Normaliza:**
   - ¿Hay datos repetidos? → Crea una tabla nueva
   - ¿Hay dependencias transitivas? → Sepáralas

---

# DIAGRAMA DE CLASES

## ¿Qué es?
Muestra las clases del sistema, sus atributos, métodos y relaciones.

## Diagrama de Clases Principal

```
┌─────────────────────────────┐
│          Book               │
├─────────────────────────────┤
│ - id: String                │
│ - title: String             │
│ - author: String            │
│ - copies: int               │
│ - availableCopies: int      │
│ - status: String            │
│ - isbn: String              │
├─────────────────────────────┤
│ + isAvailable(): boolean    │
│ + decreaseCopies(): void    │
│ + increaseCopies(): void    │
└──────────────┬──────────────┘
               │ usa
┌──────────────▼──────────────┐     ┌─────────────────────────┐
│        BookService          │     │      BookRepository      │
├─────────────────────────────┤     ├─────────────────────────┤
│ - bookRepository            │────►│ + save(BookEntity)       │
├─────────────────────────────┤     │ + findById(String)       │
│ + addBook(Book): Book       │     │ + findAll(): List        │
│ + getBook(id): Book         │     │ + existsById(String)     │
│ + getAllBooks(): List<Book>  │     │ + deleteById(String)     │
│ + updateAvailability(...)   │     └─────────────────────────┘
│ + deleteBook(id): void      │
└─────────────────────────────┘

┌─────────────────────────────┐
│          User               │
├─────────────────────────────┤
│ - id: String                │
│ - username: String          │
│ - password: String          │
│ - role: String              │
│ - email: String             │
│ - membershipType: String    │
├─────────────────────────────┤
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│         Loan                │
├─────────────────────────────┤
│ - id: String                │
│ - book: BookEntity          │
│ - user: UserEntity          │
│ - loanDate: LocalDate       │
│ - returnDate: LocalDate     │
│ - status: String            │
├─────────────────────────────┤
│ + isActive(): boolean       │
│ + isReturned(): boolean     │
└─────────────────────────────┘
```

## TIPS PARA HACER EL DIAGRAMA DE CLASES EN EL PARCIAL

1. **Una clase por entidad del negocio** → Book, User, Loan, Product, Order, etc.
2. **Atributos privados** → siempre con `-`
3. **Métodos públicos** → siempre con `+`
4. **Relaciones:**
   - Flecha continua → asociación (usa)
   - Flecha con rombo → composición (contiene)
   - Flecha punteada → dependencia (depende de)

---

# PATRONES DE DISEÑO

## ¿Qué son los patrones de diseño?
Son soluciones reutilizables a problemas comunes en el desarrollo de software.

## Patrones usados en este proyecto

### 1. Repository Pattern (Patrón Repositorio)
**¿Qué hace?** Abstrae la capa de acceso a datos.
El servicio no sabe si los datos vienen de PostgreSQL, MongoDB o memoria.

```java
// Interfaz genérica - el servicio solo conoce esto
//public interface BookRepository extends JpaRepository<BookEntity, String> {
  //  Optional<BookEntity> findByIsbn(String isbn);
//}

// El servicio usa la interfaz, no la implementación
//@Service
//public class BookService {
  //  private final BookRepository bookRepository; // no sabe si es JPA o Mongo
//}
```

**¿Cómo identificarlo en el parcial?**
> Cuando el enunciado dice "acceder a datos" o "persistir información",
> siempre aplica Repository Pattern.

---

### 2. DTO Pattern (Data Transfer Object)
**¿Qué hace?** Separa lo que se expone en la API de lo que se guarda en BD.

```java
// Lo que llega/sale por el endpoint
//public class BookDTO {
  //  private String id;
  //  private String title;
  //  private String author;
  //  private int copies;
    // NO tiene password, NO tiene campos internos
//}

// Lo que se guarda en BD
//@Entity
//public class BookEntity {
  //  private String bookId;
  //  private String title;
    // tiene más campos internos
//}
```

**¿Cómo identificarlo en el parcial?**
> Siempre usa DTOs para los endpoints. Nunca expongas la entidad directamente.

---

### 3. Mapper Pattern
**¿Qué hace?** Convierte entre DTO ↔ Modelo de dominio ↔ Entity.

```java
//@Component
//public class BookMapper {
  //  public Book toDomain(BookDTO dto) {
  //      Book book = new Book();
  //      book.setId(dto.getId());
  //      book.setTitle(dto.getTitle());
  //      return book;
  //  }

  //  public BookDTO toDTO(Book book) {
  //      BookDTO dto = new BookDTO();
  //      dto.setId(book.getId());
  //      dto.setTitle(book.getTitle());
  //      return dto;
  //  }
//}
```

---

### 4. Filter Pattern (Cadena de Filtros)
**¿Qué hace?** Intercepta cada petición antes de que llegue al controller.

```java
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
  //  @Override
  //  protected void doFilterInternal(HttpServletRequest request,
    //                                HttpServletResponse response,
    //                                 FilterChain filterChain) {
        // 1. Extrae el token del header
        // 2. Valida el token
        // 3. Carga el usuario en el contexto
        // 4. Deja pasar la petición
     //   filterChain.doFilter(request, response);
  //  }
//}
```

---

### 5. Layered Architecture (Arquitectura en Capas)
**¿Qué hace?** Organiza el código en capas con responsabilidades claras.

```
Controller  →  solo maneja HTTP
Service     →  solo maneja lógica de negocio
Repository  →  solo maneja acceso a datos
```

**Regla:** Cada capa solo conoce a la capa inmediatamente inferior.

---

## TIPS PARA IDENTIFICAR PATRONES EN EL PARCIAL

| Si el enunciado dice... | Patrón a aplicar |
|------------------------|------------------|
| "Acceder a la base de datos" | Repository Pattern |
| "Exponer datos por API" | DTO Pattern |
| "Convertir entre objetos" | Mapper Pattern |
| "Interceptar peticiones" | Filter Pattern |
| "Separar responsabilidades" | Layered Architecture |
| "Un solo objeto en toda la app" | Singleton Pattern |
| "Autenticación y autorización" | JWT + Filter + Security Config |

---

# PERSISTENCIA DUAL

## JPA (PostgreSQL) - Datos relacionales

```java
// 1. Entidad
//@Entity
//@Table(name = "books")
//public class BookEntity {
    //@Id
    //private String bookId;
    //private String title;
    //private String author;

    //@ElementCollection  // para listas simples
    //private List<String> categories;

    //@OneToMany(mappedBy = "book")  // relación 1:N
    //private List<LoanEntity> loans;
//}

// 2. Repositorio
public interface BookRepository extends JpaRepository<BookEntity, String> {
    boolean existsByIsbn(String isbn);
    List<BookEntity> findByStatus(String status);
}

// 3. Configuración en application.properties
// spring.datasource.url=jdbc:postgresql://localhost:5432/mi_db
// spring.jpa.hibernate.ddl-auto=update
```

## MongoDB - Datos no relacionales

```java
// 1. Documento
@Document(collection = "books")
public class BookDocument {
    @Id
    private String id;
    private String title;
    private List<String> categories;

    // Subdocumento embebido (no es una tabla separada)
    private Metadata metadata;

    @Data
    public static class Metadata {
        private int pages;
        private String language;
        private String publisher;
    }
}

// 2. Repositorio
public interface BookMongoRepository extends MongoRepository<BookDocument, String> { }

// 3. Configuración en application.properties
// spring.data.mongodb.uri=mongodb+srv://user:pass@cluster.mongodb.net/db
```

## Configuración dual en PersistenceConfig.java

```java
@Configuration
@EnableJpaRepositories(basePackages = "...persistence.repository")
@EnableMongoRepositories(basePackages = "...persistence.mongorepository")
public class PersistenceConfig { }
```

---

# 🔐 SEGURIDAD JWT

## Flujo completo

```
1. POST /auth/login
   Body: { "username": "admin", "password": "1234" }

2. Server valida en BD → genera token JWT

3. Response: { "token": "eyJhbGc..." }

4. Cliente guarda el token y lo usa en cada petición:
   GET /books
   Header: Authorization: Bearer eyJhbGc...

5. JwtAuthFilter intercepta → valida token → deja pasar
```

## Componentes clave

```java
// JwtService - genera y valida tokens
@Service
public class JwtService {
    public String generateToken(UserEntity user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("userId", user.getUserId())
            .claim("role", user.getRole())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }
}

// SecurityConfig - define reglas de acceso
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/books").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("LIBRARIAN","USER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

---

# ⚙️ CI/CD CON GITHUB ACTIONS

## ¿Qué es CI/CD?
- **CI (Continuous Integration):** Cada cambio se compila y prueba automáticamente
- **CD (Continuous Deployment):** Si las pruebas pasan, se despliega automáticamente

## Flujo de trabajo

```
Developer          GitHub                GitHub Actions           Azure
    │                │                        │                     │
    │── push ───────►│                        │                     │
    │                │── crea PR ────────────►│                     │
    │                │                        │── build ───────────►│
    │                │                        │── test              │
    │                │                        │── analysis          │
    │                │                        │── deploy ──────────►│
    │                │◄── pipeline verde ──────│                     │
    │◄── merge ok ───│                        │                     │
```

## Estructura del workflow (.github/workflows/ci-cd.yml)

```yaml
name: CI/CD Pipeline
on:
  pull_request:
    branches: [ main ]

jobs:
  build:   # mvn compile -DskipTests
  test:    # mvn test -Dtest="ServiceTests"
  analysis: # checkstyle + jacoco
  deploy:  # azure/webapps-deploy@v2
```

## Secrets necesarios en GitHub

| Secret | Cómo obtenerlo |
|--------|----------------|
| `AZURE_CREDENTIALS` | `az ad sp create-for-rbac ...` en terminal |
| `AZURE_WEBAPP_PUBLISH_PROFILE` | Descargar desde Azure Portal |

---

# ☁️ DEPLOY EN AZURE

## Pasos clave

1. Crear App Service (plan F1 gratis)
2. Cambiar puerto: `server.port=${PORT:80}`
3. Agregar variable de entorno `SPRING_DATA_MONGODB_URI`
4. Habilitar autenticación básica SCM en Configuración General
5. Descargar publish profile y agregarlo como secret en GitHub

## URL de la aplicación
```
https://dosw-library-api-h8bpdkatefhfd3hk.canadacentral-01.azurewebsites.net
```

## Swagger UI en producción
```
https://dosw-library-api-h8bpdkatefhfd3hk.canadacentral-01.azurewebsites.net/swagger-ui/index.html
```

---

# ✅ TESTS

## Tests de Servicio (unitarios - no necesitan BD)

```java
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;  // simula la BD

    @InjectMocks
    private BookService bookService;  // el que estamos probando

    @Test
    void dadoUnLibro_cuandoSeAgrega_entoncesSeGuardaCorrectamente() {
        // Given (dado que...)
        Book book = new Book("B1", "Clean Code", "Martin", 3);
        when(bookRepository.save(any())).thenReturn(/* entity */);

        // When (cuando...)
        Book result = bookService.addBook(book);

        // Then (entonces...)
        assertNotNull(result);
        assertEquals("B1", result.getId());
        verify(bookRepository, times(1)).save(any());
    }
}
```

## Tests de Controlador (integración - necesitan contexto Spring)

```java
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "LIBRARIAN")  // simula usuario autenticado
    void shouldCreateBook() throws Exception {
        String json = """
            {
                "id": "BC1",
                "title": "Clean Code",
                "author": "Robert Martin",
                "copies": 3,
                "isbn": "978-0132350884",
                "publicationType": "PHYSICAL"
            }
            """;
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }
}
```

## 💡 TIPS PARA TESTS EN EL PARCIAL

1. **Nombra los tests en formato GivenWhenThen:**
   `dado_que_X_cuando_Y_entonces_Z`

2. **Escenarios que siempre debes cubrir:**
   - Exitoso (happy path)
   - Elemento no encontrado (404)
   - Elemento ya existe (409 o 400)
   - Sin permisos (403)
   - Sin autenticación (401)

3. **Para tests de servicio usa `@Mock` y `@InjectMocks`**

4. **Para tests de controller usa `@SpringBootTest` + `@WithMockUser`**

5. **En CI/CD corre solo los tests de servicio** — los de controller
   necesitan BD real que no existe en GitHub Actions

---

# 🛠️ TECNOLOGÍAS

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

---

# 📎 ANEXOS

- 📄 [Documento de Arquitectura](./arquitectura-backend.md)
- 🎥 Video pruebas funcionales con MongoDB: *(pendiente)*
- 🎥 Video pruebas funcionales en Azure: *(pendiente)*
