# contas-api

API REST de **conta corrente bancária simplificada** (Banking-lite), construída como projeto de **treinamento corporativo** para praticar **Arquitetura Hexagonal (Ports & Adapters) + DDD + TDD + SOLID + Clean Code**.

> ⚠️ **Não é um produto comercial nem um sistema bancário real.** É um veículo de aprendizado. Sem compliance (PCI, LGPD, BACEN). Não use em produção.

## Objetivo

Fechar as lacunas deixadas por cursos CRUD tradicionais, exercitando na prática:

- Arquitetura Hexagonal — domínio isolado de framework (sem `@Entity` no `domain/`)
- Regra de negócio dentro do **Aggregate**, não no service
- **TDD** estrito: Red → Green → Refactor
- Modelagem por **Aggregates**, **Value Objects** e **Domain Events**
- **Java 21 Records** na fronteira (DTO / VO / Command / Event) e classe para aggregate mutável
- Segurança stateless com **JWT** + RBAC (`ADMIN` / `CLIENTE`)
- Persistência com **JPA + Flyway + Testcontainers (Postgres real)**

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.0.x (Spring Framework 7, Jakarta EE 11, Jackson 3) |
| Web | Spring WebMVC |
| Persistência | Spring Data JPA + PostgreSQL + Flyway |
| Segurança | Spring Security + JJWT 0.12.x (JWT), BCrypt |
| Docs | springdoc-openapi 3.0.x (Swagger UI) |
| Mapeamento | ModelMapper (web ↔ domain) |
| Testes | JUnit 5, WebMvcTest, Testcontainers (Postgres), REST Assured |
| Build | Maven Wrapper (`./mvnw`) |

## Arquitetura

Modular monolith com Arquitetura Hexagonal pura:

- **domain/** — POJOs, Aggregates, VOs, Domain Events, Ports. Sem imports de Spring/JPA.
- **application/** — Use Cases; orquestram aggregates + ports; recebem Commands.
- **adapters/** — Adapters de entrada (controllers REST) e saída (repositórios JPA, publishers).

Decisões-chave (ADR):

- Domain POJO + JPA Entity separados + Mapper (`domain/` sem JPA)
- `sealed interface Transacao permits Deposito, Saque, Transferencia` (exaustividade em switch)
- Transferência cross-aggregate via `@Transactional` simples (sem Saga/Outbox)
- Testcontainers Postgres real (não H2) para evitar divergências de dialeto

## Escopo funcional (v1.0)

- **Usuários** — criar, buscar, listar (paginado), alterar senha
- **Autenticação** — login → JWT (24h), filtro de validação, 401/403 por role
- **Contas** — abrir conta (ADMIN), consultar saldo (dono ou ADMIN)
- **Transações** — depositar, sacar (rejeita saldo insuficiente), transferir (atômico), extrato paginado
- **Docs** — OpenAPI 3 navegável via Swagger UI

**Fora de escopo:** frontend, multi-tenancy, Event Sourcing, microsserviços, caching, rate limiting, deploy cloud.

## Requisitos não-funcionais

- Timezone fixo `America/Sao_Paulo`, Locale `pt-BR`
- Stateless — sem sessão HTTP; JWT como única forma de auth
- Senha com BCrypt (cost 10)
- Migrations Flyway versionadas (`V<N>__*.sql`)
- `ddl-auto: validate` em todos os profiles (nunca `update`/`create`)
- `open-in-view: false`
- Cobertura JaCoCo alvo: ≥ 80% global, ≥ 90% no `domain/`

## Pré-requisitos

- JDK 21
- Docker (para PostgreSQL local e Testcontainers)

## Como rodar

```bash
# Subir PostgreSQL local (Docker Compose — a definir)
# docker compose up -d

# Rodar a aplicação
./mvnw spring-boot:run
```

Aplicação: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

## Testes

```bash
# Todos os testes (requer Docker rodando para Testcontainers)
./mvnw test

# Build completo
./mvnw clean verify
```

## Estrutura do repositório

```
contas-api/          # esta aplicação (código-fonte)
contas-api-plans/    # PRD, ROADMAP, BACKLOG, convenções, estado do projeto
```

## Documentação de projeto

Planejamento, backlog e convenções vivem em [`../contas-api-plans/`](../contas-api-plans/):

- `PRD.md` — visão, objetivos, escopo, ADRs
- `ROADMAP.md` — sprints
- `BACKLOG.md` — tasks
- `CONVENTIONS.md` — convenções de código e commits
- `STATE.md` — estado atual do projeto

## Status

🚧 Em desenvolvimento — Sprint 1 (Gestão de Usuários). Esqueleto Spring Boot inicializado.
