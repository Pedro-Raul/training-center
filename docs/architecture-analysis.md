# Clean Architecture Analysis — Training Center

## Overview

The `trainingcenter` project implements a **4-module Clean Architecture** using Quarkus with JPA/Hibernate
and MariaDB. The architecture follows the **Dependency Rule**: dependencies point inward, from outer
layers to inner layers. No inner layer knows about any outer layer.

---

## Module Dependency Graph (Maven)

```
trainingcenter-domain          ← No internal dependencies (innermost)
       ↑
trainingcenter-application     ← Depends on: domain
       ↑              ↑
trainingcenter-api       trainingcenter-infrastructure
                         ← Depends on: domain, application
```

**Dependency Rule**: Arrows point to dependencies. Inner modules are never imported by outer modules.

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│  EXTERNAL BOUNDARY                                                                                   │
│                                                                                                     │
│  ┌───────────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  API Layer (trainingcenter-api)                                                                │  │
│  │                                                                                               │  │
│  │  JAX-RS Resources (Entry Points / Ports In - Primary Adapters)                                 │  │
│  │                                                                                               │  │
│  │  ┌──────────────────────────┐  ┌──────────────────────────┐  ┌──────────────────────────┐    │  │
│  │  │ CodeforcesLinkResource   │  │ ContestResource          │  │ UserResource             │    │  │
│  │  │                          │  │                          │  │                          │    │  │
│  │  │ POST /api/codeforces/…   │  │ GET  /api/contests       │  │ POST /api/users/register │    │  │
│  │  │ GET  /api/codeforces/…   │  │ POST /api/contests       │  │                          │    │  │
│  │  └──────────┬───────────────┘  └──────────┬───────────────┘  └──────────┬───────────────┘    │  │
│  │             │                            │                            │                     │  │
│  │  ┌──────────┴───────────────┐  ┌─────────┴───────────────┐  ┌─────────┴───────────────┐    │  │
│  │  │ ExerciseResource         │  │ DailyActivityResource   │  │ UserSubmissionResource   │    │  │
│  │  │                          │  │                         │  │                          │    │  │
│  │  │ GET  /api/exercises      │  │ GET /users/{id}/…      │  │ GET /users/{id}/…        │    │  │
│  │  └──────────┬───────────────┘  └──────────┬──────────────┘  └──────────┬───────────────┘    │  │
│  │             │                            │                            │                     │  │
│  │  ┌──────────┴───────────────┐                                                    │             │  │
│  │  │ SubmissionResource       │                                                    │             │  │
│  │  │                          │                                                    │             │  │
│  │  │ POST /api/submissions    │                                                    │             │  │
│  │  └──────────┬───────────────┘                                                    │             │  │
│  │             │                                                                    │             │  │
│  │             │   ┌──────────────────────────────────────────────────────────────┐ │             │  │
│  │             └──►│  DomainExceptionMapper (JAX-RS ExceptionMapper)              │◄└─────────────┘  │  │
│  │                 │  Maps DomainException → HTTP 400/404/500                      │                 │  │
│  │                 └──────────────────────────────────────────────────────────────┘                 │  │
│  └───────────────────────────────────────────────────────────────────────────────────────────────┘  │
│                              │ Dependencies point inward                                             │
│                              ▼                                                                       │
```

```
│  ┌───────────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  Application Layer (trainingcenter-application)                                                │  │
│  │                                                                                               │  │
│  │  ┌─────────────────────────────────────────────────┐  ┌─────────────────────────────────────┐ │  │
│  │  │  PORTS IN (Use Case Interfaces)                 │  │  PORTS OUT (Repository/Client       │ │  │
│  │  │                                                 │  │  Interfaces)                        │ │  │
│  │  │  ┌───────────────────────────────────────────┐  │  │  ┌───────────────────────────────┐  │ │  │
│  │  │  │ RegisterUserUseCase                       │  │  │  │ UserRepository                │  │ │  │
│  │  │  │ CompleteCodeforcesLinkUseCase             │  │  │  │ UserProfileRepository         │  │ │  │
│  │  │  │ StartCodeforcesLinkUseCase                │  │  │  │ ExternalAccountRepository     │  │ │  │
│  │  │  │ GetCodeforcesAccountUseCase               │  │  │  │ UserSubmissionRepository      │  │ │  │
│  │  │  │ ContestUseCase                            │  │  │  │ DailyActivityRepository       │  │ │  │
│  │  │  │ GetExercisesUseCase                       │  │  │  │ ContestRepository             │  │ │  │
│  │  │  │ RecordSubmissionUseCase                   │  │  │  │ ExerciseRepository            │  │ │  │
│  │  │  │ GetDailyActivityMapUseCase                │  │  │  │ CodeforcesOidcClient          │  │ │  │
│  │  │  │ GetSubmissionsByDateUseCase               │  │  │  │ OidcStateStore                │  │ │  │
│  │  │  └───────────────────────────────────────────┘  │  │  │ PasswordHasher                │  │ │  │
│  │  │                                                 │  │  │ PendingLink (DTO)             │  │ │  │
│  │  │  ┌───────────────────────────────────────────┐  │  │  │ CodeforcesIdentity (DTO)      │  │ │  │
│  │  │  │ Use Case Implementations (Services)       │  │  │  └───────────────────────────────┘  │ │  │
│  │  │  │                                           │  │  │                                     │ │  │
│  │  │  │ RegisterUserService                       │  │  │  ┌───────────────────────────────┐  │ │  │
│  │  │  │ CodeforcesLinkService                     │  │  │  │ Commands / DTOs               │  │ │  │
│  │  │  │ ContestService                            │  │  │  │ RegisterUserCommand           │  │ │  │
│  │  │  │ ExerciseService                           │  │  │  │ RecordSubmissionCommand       │  │ │  │
│  │  │  │ UserSubmissionService                     │  │  │  │ ContestCommand                │  │ │  │
│  │  │  │ DailyActivityService                      │  │  │  └───────────────────────────────┘  │ │  │
│  │  │  └───────────────────────────────────────────┘  │  │                                     │ │  │
│  │  └─────────────────────────────────────────────────┘  └─────────────────────────────────────┘ │  │
│  └───────────────────────────────────────────────────────────────────────────────────────────────┘  │
│                              │ Dependencies point inward                                             │
│                              ▼                                                                       │
```

