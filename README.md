# tcg-store

Pokémon TCG e-commerce site: singles, graded slabs, and sealed product.

| Layer | Tech |
|---|---|
| API | Java 21, Spring Boot 3.5, Spring Data JPA, Flyway |
| Database | PostgreSQL 16 (Docker) |
| Web | React + TypeScript (Vite), not started |
| Mobile | React Native (Expo), planned last |
| Auth | OAuth login (Google/Discord) + JWT, not started |
| Payments | Stripe, not started |

---

## Status

### Day 1: foundation (done)

- [x] Monorepo scaffold (`apps/`, `packages/`, `infra/`, `docs/`)
- [x] Postgres running in Docker
- [x] `pom.xml` with Web, Data JPA, PostgreSQL driver, Flyway, Validation, springdoc
- [x] `TcgApplication` with a real `main` method
- [x] `application.yml` with datasource, JPA validate mode, Flyway, server port
- [x] `common/` shared classes filled in
- [x] `JpaConfig` with auditing enabled
- [x] `.gitignore` at repo root
- [x] App boots against an empty database and Swagger UI loads

### Next

- [x] `V2__catalog.sql` and `V3__inventory.sql` migrations
- [x] Enums: `ProductType`, `Condition`, `Grader`, `InventoryStatus`
- [x] Entities: `CardSet`, `Product`, `InventoryItem` + repositories
- [x] Seed one single, one slab, one sealed product
- [ ] `GET /products` and `GET /products/{id}`
- [ ] React app + generated API client + first catalog screen
- [ ] Then: auth, cart + reservations, checkout + Stripe, admin, mobile

---

## Repo layout

```
tcg-store/
├── apps/
│   ├── api/        Spring Boot API
│   ├── web/        React web app (not started)
│   └── mobile/     React Native (later)
├── packages/
│   ├── api-client/ generated TS client (from OpenAPI)
│   ├── types/      shared TS types
│   └── ui-logic/   shared hooks, validation, formatters
├── infra/          docker-compose.yml
├── docs/
└── .github/workflows/
```

Every scaffold file has a `TODO` comment describing what belongs in it. Delete the comment when the file is done. Find what's left with:

```powershell
grep -rn "TODO" apps packages
```

(On Windows without grep: `Get-ChildItem -Recurse apps,packages | Select-String "TODO"`)

---

## Running locally

### Prerequisites

- JDK 21
- Docker Desktop (must be **running**: wait for "Engine running")
- Maven wrapper in `apps/api` (`mvnw`, `mvnw.cmd`, `.mvn/`). Generate with `mvn wrapper:wrapper` if missing.

### 1. Start Postgres

```powershell
cd infra
docker compose up -d
```

Useful commands:

```powershell
docker compose logs -f postgres     # watch logs
docker compose down                 # stop, keep data
docker compose down -v              # stop AND wipe the database
docker exec -it tcg-postgres psql -U tcg -d tcg   # open a SQL prompt (\q to exit)
```

`infra/docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: tcg-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: tcg
      POSTGRES_USER: tcg
      POSTGRES_PASSWORD: tcg_dev_password
    ports:
      - "5433:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U tcg -d tcg"]
      interval: 5s
      timeout: 3s
      retries: 10

volumes:
  pgdata:
```

### 2. Start the API

```powershell
cd apps/api
.\mvnw spring-boot:run
```

Success looks like:

```
Tomcat started on port 8080
Started TcgApplication in ... seconds
```

Then open Swagger UI: <http://localhost:8080/swagger-ui.html>
(It will list no endpoints until controllers exist.)

Stop with **Ctrl+C**.

---

## Configuration

`apps/api/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/tcg
    username: tcg
    password: tcg_dev_password
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  flyway:
    enabled: true
```

### Why these ports

This machine already has other things on the default ports, so the project uses non-default ones:

| Service | Default | Used here | Reason |
|---|---|-----------|---|
| Postgres | 5432 | **5433**  | A local Windows Postgres install owns 5432 |
| API | 8080 | **8080**  | 

Anything that talks to the database (IDE database tool, DBeaver, pgAdmin) must use port **5433**.

### What `ddl-auto: validate` means

Hibernate checks that your `@Entity` classes match the database tables and **changes nothing**. Flyway migrations are the single source of truth for the schema. If you add an entity field and forget the migration, the app fails at startup with a message naming the missing table/column.

`spring.jpa.hibernate.ddl-auto` lives under `jpa → hibernate`, a sibling of `datasource`, not inside it.

### Postgres credentials

`POSTGRES_USER`, `POSTGRES_PASSWORD`, and `POSTGRES_DB` are variables defined by the official Postgres image, and you choose the values. They're only read when the volume is **empty** (first start). Changing them later has no effect until you run `docker compose down -v`.

---

## What's built so far

### `com.yourco.tcg` (apps/api/src/main/java)

| File | Purpose |
|---|---|
| `TcgApplication` | `@SpringBootApplication` entry point with `main`. Must stay in the root package so component scanning finds everything below it. |
| `config/JpaConfig` | `@EnableJpaAuditing` so `createdAt` / `updatedAt` populate automatically. |
| `common/BaseEntity` | `@MappedSuperclass` abstract class: `id`, `createdAt`, `updatedAt`. Entities extend it. |
| `common/ApiError` | Record: the JSON shape of every error response. Has `of(...)` static factory methods that fill in the timestamp and null field errors. |
| `common/GlobalExceptionHandler` | `@RestControllerAdvice` mapping exceptions to `ApiError` responses. |
| `common/NotFoundException` | → 404 `NOT_FOUND` |
| `common/ConflictException` | → 409 `CONFLICT` (duplicates, clashing state) |
| `common/OutOfStockException` | → 409 `OUT_OF_STOCK` (frontend can show a specific message) |
| `common/PageResponse<T>` | Record wrapping paginated results: `content`, `page`, `size`, `totalElements`, `totalPages`. |

### Error handling summary

| Exception | HTTP | `code` |
|---|---|---|
| `NotFoundException` | 404 | `NOT_FOUND` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_FAILED` (with `fieldErrors`) |
| `ConflictException` | 409 | `CONFLICT` |
| `OutOfStockException` | 409 | `OUT_OF_STOCK` |
| `DataIntegrityViolationException` (optional) | 409 | `DATA_CONFLICT` |
| anything else | 500 | `INTERNAL_ERROR` (generic message, no internals leaked) |

### Migrations

`src/main/resources/db/migration/V1`–`V5` currently contain only comments (valid but create no tables). **Edit them before the first run that has real entities.** Flyway records a checksum for every applied migration, so editing an applied file causes a checksum-mismatch failure. In early development, reset with:

```powershell
cd infra
docker compose down -v
docker compose up -d
```

Rule going forward: never edit an applied migration; add a new one.

---

## Design decisions

- **Modular monolith**, package by feature (`catalog/`, `inventory/`, `order/`, ...), not by layer.
- **Controllers are thin**; services hold logic; entities never leave the service layer; DTOs are records.
- **Entities are classes, not records** (JPA needs a no-arg constructor and mutable, non-final fields).
- **Enums on entities** use `@Enumerated(EnumType.STRING)`.
- **Money is integer cents.** Snapshot price on `order_items`.
- **Each slab is its own inventory row** (quantity 1, unique cert number).
- **Atomic stock decrement:** `UPDATE ... SET quantity = quantity - :n WHERE id = :id AND quantity >= :n`. Zero rows updated means out of stock.
- **Reservations** hold stock for 10-15 minutes during checkout.
- **Token-based auth** (JWT + rotating refresh tokens) so the mobile app can reuse the same API.
- **Table named `orders`**, not `order` (reserved SQL word).

---

## Troubleshooting log (things that went wrong on day 1)

| Symptom | Cause | Fix |
|---|---|---|
| No `pom.xml` | Scaffold only had source stubs | Added `pom.xml` to `apps/api/` |
| `Unable to find a suitable main class` | `TcgApplication` was an empty stub | Added `main` with `SpringApplication.run` |
| `Driver class org.postgresql.Driver not found` | IDE hadn't loaded `apps/api/pom.xml` as a Maven project | Reload Maven project / open `apps/api` as the project |
| `password authentication failed for user "tcg"` | Local Windows Postgres (`postgres.exe`) owned port 5432, so the app never reached the container | Moved container to **5433**, updated URL |
| `error during connect ... dockerDesktopLinuxEngine` | Docker Desktop wasn't running | Start Docker Desktop, wait for "Engine running", check `docker version` |

Handy diagnostics:

```powershell
netstat -ano | findstr :<port>        # who owns a port
tasklist /FI "PID eq <pid>"           # what that process is
docker ps                             # is the container up, on which port
docker version                        # is the Docker engine reachable
```

---

## Git

- One `.gitignore` at the repo root covering Maven (`target/`), Node (`node_modules/`, `dist/`), IDE folders, OS files, and **secrets** (`.env`, `application-local.yml`).
- **Commit:** `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/`, lockfiles, `docker-compose.yml`, migration files.
- **Never commit:** real passwords, Stripe keys, OAuth client secrets, JWT signing keys. If one is committed, treat it as exposed and rotate it.
- If ignored files were already tracked: `git rm -r --cached <path>` then commit.
- Consider keeping the repo outside OneDrive (e.g. `C:\dev\tcg-store`). OneDrive syncing `.git`, `target/`, and `node_modules/` can corrupt repos and lock files.

---

## Production notes (for later)

- Use a **non-superuser** Postgres role for the app; run Flyway with a more privileged one.
- Real secrets come from environment variables or a secret store, never from committed files.
- Webhooks from Stripe must be idempotent (store processed event ids).
- Decide the order-history retention window up front; keep accounting records separately if legally required.