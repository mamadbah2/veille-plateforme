# Backend Analysis: Horus (Veille SSI)

## Overview
The backend is designed as a monolithic Spring Boot application for a cybersecurity watch platform. It follows a clean, layered architecture (Web, Service, Data) and adheres to SOLID principles.

## Technology Stack
- **Language:** Java 21
- **Framework:** Spring Boot 4.0.2 (Released Jan 2026)
- **Database:** MongoDB
- **Build Tool:** Maven
- **Dependencies:**
  - `spring-boot-starter-webmvc` (REST API)
  - `spring-boot-starter-data-mongodb` (Persistence)
  - `spring-boot-starter-security` + `jjwt` (Security/Auth - partially configured)
  - `springdoc-openapi` (Swagger/OpenAPI documentation)
  - `mapstruct` (DTO mapping)
  - `lombok` (Code generation)

## Architecture & Structure
The project follows a standard package structure under `sn.ssi.veille`:
- **`config`**: Configuration classes (`MongoConfig`, `WebClientConfig`, `SourceInitializer`).
- **`exceptions`**: Comprehensive custom exception handling (`GlobalExceptionHandler`).
- **`models`**:
  - `entities`: MongoDB documents (`Article`, `User`, `Source`, etc.).
  - `repositories`: Spring Data MongoDB repositories.
- **`services`**: Business logic interfaces and implementations (`ScrapingServiceImpl`, `SourceServiceImpl`).
- **`web`**: REST Controllers (`SourceController`, `ScrapingController`).

## Implementation State (Feature/Scraping)

### ✅ Implemented
- **Domain Models:** Comprehensive Source entity (23 fields).
- **Services:** Full CRUD for Sources, Scraping logic for RSS and APIs (NIST, HN).
- **Security:** CSRF protection, WebClient timeouts, HTML sanitization, Transactional services.
- **Resilience:** Exponential backoff for scraping failures.
- **DevOps:** Auto-seeding of default sources.

### ⚠️ Pending (Phase 3)
- **AI Classification:** Integration with Local LLM (LM Studio) for article categorization.
- **Cross-Referencing:** Deduplication and correlation logic.

## Recommendations
1.  **AI Integration:** Proceed with Phase 3 using localhost:1234.
2.  **Testing:** Add unit tests for `ScrapingServiceImpl`.
