# Architectural Analysis Report — Training Center API

> **Project**: Quarkus-based Competitive Programming Training Center  
> **Architecture Style**: Clean Architecture / Domain-Driven Design (DDD)  
> **Date**: November 11, 2026  
> **Status**: Comprehensive gap analysis complete

---

## 1. Module Structure Overview

```
trainingcenter/
├── trainingcenter-domain/          # Domain entities, value objects, exceptions
├── trainingcenter-application/     # Use case ports, services, queries
├── trainingcenter-infrastructure/  # JPA adapters, external services, security
└── trainingcenter-api/             # REST resources, DTOs, configuration
```

| Module | Responsibility | Dependencies |
|---|---|---|
| `trainingcenter-domain` | Domain model, business rules, exceptions | None (pure) |
| `trainingcenter-application` | Use case interfaces, service implementations, queries | → domain |
| `trainingcenter-infrastructure` | JPA entities/mappers, OIDC client, password hashing | → domain, application |
| `trainingcenter-api` | REST endpoints, DTOs, application config | → domain, application, infrastructure |

---

## 2. Domain Layer Audit

### 2.1 Entities (7 total — ✅ Complete)

| Entity | Package | Factory Methods | Business Rules |
|---|---|---|---|
| `User` | `co.ufps.trainingcenter.account` | `register()`, `restore()` | Email/password validation, role enforcement |
| `UserProfile` | `co.ufps.trainingcenter.account` | `generate()`, `restore()` | Streak tracking, rating validation |
| `ExternalAccount` | `co.ufps.trainingcenter.account` | `link()`, `restore()` | Platform linking, rating refresh |
| `UserSubmission` | `co.ufps.trainingcenter.account` | Constructor only | Submission recording |
| `DailyActivity` | `co.ufps.trainingcenter.activity` | `register()`, `restore()`, `startOf()` | Submission counting, solved tracking |
| `Contest` | `co.ufps.trainingcenter.contest` | `restore()` | Time-based status updates |
| `Exercise` | `co.ufps.trainingcenter.exercise` | `create()`, `restore()` | Problem catalog management |

### 2.2 Value Objects & Enums (6 total — ✅ Complete)

| Type | Values |
|---|---|
| `Role` | `USER`, `ADMIN` |
| `Status` (Contest) | `UPCOMING`, `ACTIVE`, `FINISHED`, `CANCELLED` |
| `Difficulty` | `EASY`, `MEDIUM`, `HARD`, `LEGENDARY` |
| `Verdict` | `ACCEPTED`, `WRONG_ANSWER`, `TIME_LIMIT_EXCEEDED`, `COMPILATION_ERROR`, `RUNTIME_ERROR` |
| ID types | `UserId`, `ContestId`, `ExerciseId`, `UserSubmissionId`, `DailyActivityId`, `UserProfileId` |

### 2.3 Domain Exceptions (3 total — ✅ Complete)

| Exception | Purpose |
|---|---|
| `DomainException` | Base domain validation error |
| `OidcAuthenticationException` | OIDC flow errors |
| `DuplicateEmailException` | Registration conflict |

---

## 3. Application Layer Audit

### 3.1 Use Case Ports (12 interfaces — ✅ Complete)

| Port Interface | Method | Purpose |
|---|---|---|
| `RegisterUserUseCase` | `register(RegisterUserCommand)` | User registration |
| `ContestUseCase` | `getActiveContests()` | List active contests |
| `SearchContestsUseCase` | `search(ContestFilter, PageRequest)` | Paginated contest search |
| `GetExercisesUseCase` | `getExercisesByTopic(topic)` | Filter exercises by topic |
| `RecordSubmissionUseCase` | `recordSubmission(userId, date, solved)` | Record a submission |
| `SearchSubmissionsUseCase` | `search(userId, filter, pageRequest)` | Paginated submission search |
| `GetSubmissionsByDateUseCase` | `getSubmissionsByDate(userId, from, to)` | Grouped submission counts |
| `GetDailyActivityMapUseCase` | `getActivityMap(userId, from, to)` | Daily activity timeline |
| `StartCodeforcesLinkUseCase` | `startLink(userId)` | Begin Codeforces OAuth |
| `CompleteCodeforcesLinkUseCase` | `completeLink(code, state)` | Complete Codeforces OAuth |
| `GetCodeforcesAccountUseCase` | `getByUserId(userId)` | Get linked Codeforces account |

### 3.2 Service Implementations (9 services — ✅ Complete)

| Service | Implements | Notes |
|---|---|---|
| `RegisterUserService` | `RegisterUserUseCase` | Full registration with profile creation |
| `ContestService` | `ContestUseCase` | Time-based status update on retrieval |
| `ContestSearchService` | `SearchContestsUseCase` | Paginated search with filters |
| `ExerciseService` | `GetExercisesUseCase` | Topic-based filtering |
| `UserSubmissionService` | `RecordSubmissionUseCase`, `GetSubmissionsByDateUseCase` | Two use cases in one service |
| `SubmissionSearchService` | `SearchSubmissionsUseCase` | Paginated submission search |
| `DailyActivityService` | `GetDailyActivityMapUseCase` | Activity map retrieval |
| `CodeforcesLinkService` | `StartCodeforcesLinkUseCase`, `CompleteCodeforcesLinkUseCase`, `GetCodeforcesAccountUseCase` | Three use cases in one service |

### 3.3 Repository Ports (8 interfaces — ✅ Complete)

| Port | Key Methods |
|---|---|
| `UserRepository` | `findById`, `findByEmail`, `findByUsername`, `save` |
| `UserProfileRepository` | `findByUserId`, `save` |
| `ExternalAccountRepository` | `findByUserId`, `findByPlatformAndExternalId`, `save` |
| `UserSubmissionRepository` | `save`, `findByUserId`, `findByUserIdAndDateRange`, `findByUserIdAndExerciseId` |
| `DailyActivityRepository` | `findByUserIdAndDateRange`, `findByUserIdAndDate`, `save` |
| `ContestRepository` | `findAll`, `findById`, `save` |
| `ExerciseRepository` | `findAll`, `findById`, `findByTopic`, `save` |
| `CodeforcesOidcClient` | `buildAuthorizationUrl`, `exchangeCode` |

### 3.4 Query Objects (4 total — ✅ Complete)

| Object | Purpose |
|---|---|
| `PageRequest` | Pagination + sorting configuration |
| `PageResult<T>` | Paginated response wrapper |
| `ContestFilter` | Contest search criteria |
| `SubmissionFilter` | Submission search criteria |

### 3.5 Supporting Ports (2 total — ✅ Complete)

| Port | Implementation |
|---|---|
| `PasswordHasher` | `BcryptPasswordHasher` |
| `OidcStateStore` | `InMemoryOidcStateStore` |

---

## 4. Infrastructure Layer Audit

### 4.1 JPA Entities & Mappers (7 pairs — ✅ Complete)

| JPA Entity | Domain Entity | Mapper Method |
|---|---|---|
| `UserJpa` | `User` | `toDomain()` / `toJpa()` |
| `UserProfileJpa` | `UserProfile` | `toDomain()` / `toJpa()` |
| `ExternalAccountJpa` | `ExternalAccount` | `toDomain()` / `toJpa()` |
| `UserSubmissionJpa` | `UserSubmission` | `toDomain()` / `toJpa()` |
| `DailyActivityJpa` | `DailyActivity` | `toDomain()` / `toJpa()` |
| `ContestJpa` | `Contest` | `toDomain()` / `toJpa()` |
| `ExerciseJpa` | `Exercise` | `toDomain()` / `toJpa()` |

### 4.2 Repository Implementations (7 JPA repos — ✅ Complete)

| Implementation | Repository Port | Notes |
|---|---|---|
| `UserRepositoryJpa` | `UserRepository` | Complete |
| `UserProfileRepositoryJpa` | `UserProfileRepository` | Complete |
| `ExternalAccountRepositoryJpa` | `ExternalAccountRepository` | Complete |
| `UserSubmissionRepositoryJpa` | `UserSubmissionRepository` | Complete |
| `DailyActivityRepositoryJpa` | `DailyActivityRepository` | Complete |
| `ContestRepositoryJpa` | `ContestRepository` | Complete |
| `ExerciseRepositoryJpa` | `ExerciseRepository` | Complete |

### 4.3 External Service Adapters (2 total — ✅ Complete)

| Adapter | Port | Notes |
|---|---|---|
| `CodeforcesOidcClientAdapter` | `CodeforcesOidcClient` | OIDC discovery, token exchange, HS256 verification |
| `BcryptPasswordHasher` | `PasswordHasher` | BCrypt hashing |

---

## 5. API Layer Audit

### 5.1 REST Resources (10 total — ✅ Complete)

| Resource | Path | Methods | Use Cases |
|---|---|---|---|
| `UserResource` | `/api/users` | POST `/register` | `RegisterUserUseCase` |
| `ContestResource` | `/api/contests` | GET `/active` | `ContestUseCase` |
| `ContestSearchResource` | `/api/contests/search` | GET | `SearchContestsUseCase` |
| `ExerciseResource` | `/api/exercises` | GET | `GetExercisesUseCase` |
| `SubmissionResource` | `/api/submissions` | POST `/{userId}` | `RecordSubmissionUseCase` |
| `UserSubmissionResource` | `/users/{userId}/submissions/by-date` | GET | `GetSubmissionsByDateUseCase` |
| `UserSubmissionSearchResource` | `/users/{userId}/submissions` | GET | `SearchSubmissionsUseCase` |
| `DailyActivityResource` | `/users/{userId}/daily-activity` | GET | `GetDailyActivityMapUseCase` |
| `CodeforcesLinkResource` | `/users/{userId}/codeforces` | POST `/link`, GET | `StartCodeforcesLinkUseCase`, `GetCodeforcesAccountUseCase` |
| `CodeforcesCallbackResource` | `/codeforces/callback` | GET | `CompleteCodeforcesLinkUseCase` |

### 5.2 DTOs & Helpers (3 total — ✅ Complete)

| Type | Purpose |
|---|---|
| `PageResponse<T>` | Paginated API response |
| `QueryParams` | Query parameter parsing utility |
| Inline records | Each resource defines its own request/response records |

---

## 6. Configuration

### application.properties
```properties
quarkus.datasource.db-kind = mariadb
quarkus.datasource.jdbc.url = jdbc:mariadb://54.221.17.1:3306/training%20center
quarkus.hibernate-orm.schema-management.strategy = none
```
- **Database**: MariaDB on remote host (54.221.17.1)
- **Schema management**: Disabled (manual schema expected)
- **OIDC config**: Read from `codeforces.oidc.*` properties at runtime

---

## 7. Gap Analysis — CRITICAL FINDINGS

### 7.1 🔴 CRITICAL: Architecture Violations

#### Violation 1: `SubmissionResource` bypasses Use Case layer
**File**: `SubmissionResource.java` (lines 57-81)

The resource directly injects and uses `UserSubmissionRepository` alongside `RecordSubmissionUseCase`:

```java
// ❌ Architecture violation — direct repository access in API layer
private final UserSubmissionRepository userSubmissionRepository;

@POST
@Path("/{userId}")
public SubmissionResponse record(...) {
    UserSubmission submission = new UserSubmission(...);
    userSubmissionRepository.save(submission);  // ← Bypasses use case
    DailyActivity activity = recordSubmissionUseCase.recordSubmission(...);
    return SubmissionResponse.from(submission);
}
```

**Fix**: Create a `RecordSubmissionCommand` and move all submission logic into the use case.

#### Violation 2: `ExerciseResource` exposes Domain entities directly
**File**: `ExerciseResource.java` (line 25-26)

```java
@GET
public List<Exercise> getExercises(@QueryParam("topic") String topic) {
    return getExercisesUseCase.getExercisesByTopic(topic);  // ← Returns domain entity
}
```

**Fix**: Create an `ExerciseResponse` DTO and map domain entities before returning.

### 7.2 🔴 CRITICAL: Missing Security Layer

- **No authentication**: All endpoints are publicly accessible
- **No authorization**: No role-based access control (RBAC)
- **No request filtering**: No `@RolesAllowed`, `@PermitAll`, or custom security
- **No CSRF protection**: State-changing operations unprotected

### 7.3 🟠 HIGH: Missing Use Cases & Endpoints

| Missing Feature | Impact | Priority |
|---|---|---|
| **User Login / Authentication** | No way for users to sign in | 🔴 Critical |
| **User Profile Management** | Cannot update username, email, or role | 🟠 High |
| **Get User Profile** | Cannot retrieve user stats (rating, streaks) | 🟠 High |
| **Create Contest** | Contests cannot be created via API | 🟠 High |
| **Update Contest** | Contests cannot be modified | 🟡 Medium |
| **Delete Contest** | Contests cannot be removed | 🟡 Medium |
| **Create Exercise** | Exercises cannot be added via API | 🟠 High |
| **Update Exercise** | Exercises cannot be modified | 🟡 Medium |
| **Delete Exercise** | Exercises cannot be removed | 🟡 Medium |
| **Get Exercise by ID** | Cannot retrieve single exercise | 🟡 Medium |
| **Bulk Import Submissions** | No batch import from external platforms | 🟡 Medium |
| **Sync External Account** | No manual refresh of Codeforces data | 🟡 Medium |
| **Unlink External Account** | Cannot disconnect Codeforces | 🟡 Medium |
| **Admin User Management** | No admin CRUD for users | 🟡 Medium |

### 7.4 🟠 HIGH: Infrastructure Concerns

#### Issue 1: InMemoryOidcStateStore is not production-ready
**File**: `InMemoryOidcStateStore.java`

```java
@ApplicationScoped
public class InMemoryOidcStateStore implements OidcStateStore {
    private final Map<String, OidcState> states = new ConcurrentHashMap<>();
    // No TTL, no cleanup, no persistence
}
```

- **Problem**: States are lost on server restart; memory leaks if not cleaned
- **Fix**: Use database-backed or Redis-backed state store with TTL

#### Issue 2: Duplicate import in CodeforcesOidcClientAdapter
**File**: `CodeforcesOidcClientAdapter.java` (lines 8, 25, 26)

```java
import com.nimbusds.jose.crypto.MACVerifier;  // line 8
import com.nimbusds.jose.crypto.MACVerifier;  // line 25 (duplicate)
import com.nimbusds.jose.crypto.MACVerifier;  // line 26 (triple duplicate)
```

#### Issue 3: No schema management
```properties
quarkus.hibernate-orm.schema-management.strategy = none
```

- **Problem**: No Flyway, Liquibase, or auto-DDL for database migrations
- **Fix**: Add Flyway or enable `quarkus.hibernate-orm.database.generation` for dev

### 7.5 🟡 MEDIUM: API Design Issues

| Issue | Location | Recommendation |
|---|---|---|
| Inconsistent base paths | `/api/users/...` vs `/users/...` | Standardize all to `/api/...` |
| No API versioning | All endpoints at root | Add `/api/v1/` prefix |
| No rate limiting | All endpoints unlimited | Add rate limiting for auth endpoints |
| No input validation | Command objects lack `@Valid` | Add Bean Validation annotations |



### 7.7 🟢 LOW: Minor Issues

| Issue | Location | Recommendation |
|---|---|---|
| `User.register()` unused parameter | `initialProfileName` parameter not used | Remove or use it |
| No `equals()`/`hashCode()` on entities | Domain entities | Add for comparison |
| No unit tests visible | Test directories | Add JUnit tests for services |
| No integration tests | Test directories | Add Quarkus test extensions |
| Database credentials in properties | `application.properties` | Use env vars or vault |

---

## 8. Implementation Completeness Matrix

### 8.1 Domain → Application → Infrastructure → API Traceability

| Domain Entity | Use Cases | JPA Repo | API Endpoints | Status |
|---|---|---|---|---|
| **User** | RegisterUserUseCase | UserRepositoryJpa | POST /api/users/register | ⚠️ Read-only |
| **UserProfile** | (via RegisterUserService) | UserProfileRepositoryJpa | None | ⚠️ No direct API |
| **ExternalAccount** | Start/Complete/Get Codeforces | ExternalAccountRepositoryJpa | POST /users/{id}/codeforces/link, GET | ✅ Complete |
| **UserSubmission** | Record/Search/ByDate | UserSubmissionRepositoryJpa | POST /api/submissions/{id}, GET /users/{id}/submissions | ✅ Complete |
| **DailyActivity** | GetDailyActivityMapUseCase | DailyActivityRepositoryJpa | GET /users/{id}/daily-activity | ✅ Complete |
| **Contest** | ContestUseCase, SearchContests | ContestRepositoryJpa | GET /api/contests/active, GET /api/contests/search | ⚠️ Read-only |
| **Exercise** | GetExercisesUseCase | ExerciseRepositoryJpa | GET /api/exercises | ⚠️ Read-only |

### 8.2 CRUD Matrix by Aggregate

| Aggregate | Create | Read | Update | Delete | Search |
|---|---|---|---|---|---|
| User | ✅ Register | ❌ | ❌ | ❌ | ❌ |
| UserProfile | ✅ (auto) | ❌ | ❌ | ❌ | ❌ |
| ExternalAccount | ✅ Link | ✅ | ❌ | ❌ | ❌ |
| UserSubmission | ✅ Record | ✅ | ❌ | ❌ | ✅ |
| DailyActivity | ✅ (auto) | ✅ | ❌ | ❌ | ❌ |
| Contest | ❌ | ✅ | ❌ | ❌ | ✅ |
| Exercise | ❌ | ✅ | ❌ | ❌ | ❌ |

---

## 9. Recommendations — Prioritized Action Plan

### Phase 1: Critical Fixes (Week 1)
1. **Fix architecture violations**:
   - Move `SubmissionResource` repository call into use case
   - Add `ExerciseResponse` DTO in `ExerciseResource`
2. **Add global error handling**:
   - Create `ExceptionMapper<DomainException>` for 400 errors
   - Create `ExceptionMapper<NotFoundException>` for 404 errors
   - Create generic `ExceptionMapper<Throwable>` for 500 errors
3. **Fix duplicate imports** in `CodeforcesOidcClientAdapter`

### Phase 2: Security & Authentication (Week 2)
4. **Add authentication**:
   - Implement `LoginUseCase` with session/JWT token generation
   - Add `POST /api/auth/login` and `POST /api/auth/logout` endpoints
5. **Add authorization**:
   - Enable Quarkus security (`quarkus-smallrye-jwt` or `quarkus-oidc`)
   - Add `@RolesAllowed` to admin-only endpoints
6. **Replace InMemoryOidcStateStore**:
   - Implement database-backed or Redis-backed state store

### Phase 3: Missing CRUD Operations (Week 3-4)
7. **Add user management endpoints**:
   - `GET /api/users/{userId}` — Get user profile
   - `PUT /api/users/{userId}` — Update user
   - `GET /api/users/{userId}/profile` — Get detailed stats
8. **Add contest management** (admin-only):
   - `POST /api/contests` — Create contest
   - `PUT /api/contests/{contestId}` — Update contest
   - `DELETE /api/contests/{contestId}` — Delete contest
9. **Add exercise management** (admin-only):
   - `POST /api/exercises` — Create exercise
   - `PUT /api/exercises/{exerciseId}` — Update exercise
   - `DELETE /api/exercises/{exerciseId}` — Delete exercise
   - `GET /api/exercises/{exerciseId}` — Get single exercise

### Phase 4: Production Readiness (Week 5-6)
10. **Database migrations**:
    - Add Flyway with baseline migration
    - Or enable `quarkus.hibernate-orm.database.generation=update` for dev
11. **API standardization**:
    - Standardize base paths to `/api/v1/...`
    - Add Bean Validation (`@Valid`, `@NotNull`, etc.)
    - Add API versioning strategy
12. **Testing**:
    - Add unit tests for all service classes
    - Add integration tests with `@QuarkusTest`
    - Add REST endpoint tests with `@QuarkusTestResource`
13. **Security hardening**:
    - Move database credentials to environment variables
    - Add rate limiting for authentication endpoints
    - Add CORS configuration

---

## 10. Summary

| Category | Count | Status |
|---|---|---|
| Domain entities | 7 | ✅ Complete |
| Use case ports | 12 | ✅ Complete |
| Service implementations | 9 | ✅ Complete |
| Repository ports | 8 | ✅ Complete |
| JPA adapters | 7 | ✅ Complete |
| REST resources | 10 | ✅ Complete |
| External adapters | 2 | ✅ Complete |

**Overall Assessment**: The project has a **solid foundation** with well-structured Clean Architecture. The Domain, Application, and Infrastructure layers are largely complete for the current scope. However, there are **2 critical architecture violations**, **13 missing use cases/endpoints**, and **several production-readiness gaps** that need to be addressed before deployment.

**Estimated effort to reach production-ready**: 4-6 weeks with the recommended phased approach above.
