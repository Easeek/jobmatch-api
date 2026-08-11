# AGENTS.md — 취업 정보 추천·탐색 서비스 개발 지침

이 파일은 Codex(및 다른 AI 코딩 에이전트)가 이 저장소에서 작업할 때 자동으로 읽는 지침 파일이다.
작업을 시작하기 전에 `docs/erd.md`와 `docs/api-spec.md`를 반드시 먼저 읽는다.

---

## 1. 기술 스택 (고정값 — 임의로 바꾸지 말 것)

- Language: Java 17
- Framework: Spring Boot 3.x
- ORM: Spring Data JPA (Hibernate)
- DB: PostgreSQL 16 (배포는 Render 무료 관리형 PostgreSQL, 로컬/개발은 Docker Compose로 실행 — 운영과 로컬을 동일한 DB 엔진으로 맞춘다. `ddl-auto: validate` 고정 — 스키마는 직접 DDL로 관리하고 Hibernate가 자동 생성하지 않는다)
- 빌드 도구: Maven (`pom.xml`)
- API 문서화: springdoc-openapi (Swagger UI)
- 인증: 일반 사용자는 `sessionKey`(UUID, 요청 바디/쿼리로 전달), 관리자는 JWT

## 2. 패키지 구조

```
src/main/java/com/project/jobmatch/
├── domain/
│   ├── user/              (User, UserCondition, ...)
│   ├── job/                (Job, JobPosting, ...)
│   ├── training/            (TrainingCourse, ...)
│   ├── support/             (SupportProgram, ...)
│   ├── recommendation/      (RecommendationResult, RecommendationItem, RecommendationCriteria)
│   ├── saved/               (SavedJob, SavedTraining)
│   └── admin/               (DataSyncLog)
├── common/
│   ├── response/            (공통 응답 포맷 ApiResponse<T>, ErrorResponse)
│   ├── exception/           (GlobalExceptionHandler, CustomException 계열)
│   └── config/              (SwaggerConfig, SecurityConfig, JpaConfig)
└── JobMatchApplication.java
```

각 도메인 패키지 내부는 `entity / repository / dto / service / controller` 5개 하위 패키지로 나눈다.

## 3. 구현 순서 (반드시 이 순서를 지킬 것)

1. **공통 응답 포맷 먼저 구현**: `docs/api-spec.md` 0번 섹션의 성공/에러 JSON 포맷에 맞는 `ApiResponse<T>`, `ErrorResponse`, `GlobalExceptionHandler`부터 만든다. 이후 모든 컨트롤러는 이 포맷을 사용한다.
2. **마스터 데이터 엔티티**: `region`, `interest_field`, `job`, `training_course`, `support_program`, 그리고 N:M 매핑 테이블(`job_interest_field`, `job_work_type` 등) — `docs/erd.md`의 컬럼 정의를 그대로 따른다.
3. **사용자 조건 입력**: `user`, `user_condition`, `user_condition_interest_field` → `POST /conditions`, `GET /conditions/{id}`, `GET /meta/conditions`
4. **추천 로직**: `recommendation_criteria`(가중치 마스터) → `recommendation_result`, `recommendation_item` → `docs/api-spec.md` 2번 섹션. 매칭 점수 계산 로직은 `docs/erd.md` 5번 섹션의 공식을 그대로 구현한다.
5. **정보 탐색 조회 API**: `/jobs`, `/trainings`, `/postings`, `/support-programs` (3번 섹션 마스터 데이터 기반 조회, 병렬 진행 가능)
6. **저장/비교**: `saved_job`, `saved_training`, `/jobs/compare` (4번 섹션)
7. **관리자 API**: JWT 로그인 → 데이터 CRUD → `recommendation_criteria` 관리 → `data_sync_log` (5번 섹션, 마지막에 진행)

각 단계는 **Entity → Repository → DTO → Service → Controller** 순서로 만든다. 이 순서를 건너뛰지 말 것.

`sql/schema.sql`을 작성할 때는 `docs/erd.md`의 컬럼 정의를 PostgreSQL 문법으로 변환한다. 대응 규칙은 `docs/erd.md` 최하단의 "PostgreSQL 타입 매핑" 표를 따른다 (예: `BIGINT AUTO_INCREMENT` → `BIGINT GENERATED ALWAYS AS IDENTITY`, `DATETIME` → `TIMESTAMP`).

## 4. 코딩 컨벤션

- Enum은 Java enum으로 정의하되 DB 컬럼은 전부 `@Enumerated(EnumType.STRING)` + `VARCHAR`로 매핑한다. `docs/erd.md` 4번 섹션의 값 목록을 그대로 사용한다.
- 모든 엔티티는 `created_at`(및 필요 시 `updated_at`)을 갖고, `@CreatedDate`/`@LastModifiedDate` + `@EnableJpaAuditing`으로 자동 처리한다.
- 컨트롤러는 얇게 유지하고, 비즈니스 로직은 Service에 둔다.
- DTO는 요청용(`XxxRequest`)과 응답용(`XxxResponse`)을 분리한다. 엔티티를 API 응답에 직접 노출하지 않는다.
- 예외는 `CustomException` 계열로 던지고, `docs/api-spec.md`의 에러 코드(`JOB_NOT_FOUND`, `ALREADY_SAVED` 등)를 그대로 사용한다.
- N:M 관계는 매핑 테이블을 그대로 `@ManyToMany` 대신 명시적 조인 엔티티나 `@ElementCollection`으로 구현해도 무방하되, 조회 성능을 고려해 필요한 곳만 즉시 로딩(EAGER)한다.

## 5. Swagger

- `springdoc-openapi-starter-webmvc-ui` 의존성을 추가하면 별도 설정 없이 `/swagger-ui/index.html`에서 확인 가능하다.
- 각 컨트롤러 메서드에는 `@Operation(summary = "...")`을 붙여 `docs/api-spec.md`의 설명을 그대로 옮긴다.
- 관리자 API는 `@SecurityRequirement(name = "bearerAuth")`로 표시한다.

## 6. 완료 기준 (Definition of Done)

기능 하나를 구현했다고 보고하기 전에 다음을 모두 만족해야 한다.

- [ ] `docs/api-spec.md`에 정의된 요청/응답 JSON 구조와 정확히 일치한다.
- [ ] 정상 케이스와 최소 1개의 에러 케이스(404/409 등)를 처리한다.
- [ ] `mvn spring-boot:run` 또는 `docker compose up`으로 기동 후 Swagger UI에서 직접 호출해 정상 동작을 확인했다.
- [ ] 새로 추가한 엔티티에 대응하는 테이블 DDL이 `docs/erd.md`(PostgreSQL 문법으로 변환된 상태)와 일치한다 (스키마는 `ddl-auto: validate`이므로 반드시 DDL을 먼저 반영해야 애플리케이션이 기동된다).

## 7. 하지 말아야 할 것

- `ddl-auto`를 `update`나 `create`로 바꾸지 않는다.
- `docs/erd.md`, `docs/api-spec.md`에 없는 필드/엔드포인트를 임의로 추가하지 않는다. 필요하면 먼저 사람에게 확인을 요청한다.
- 인증이 필요 없는 API에 JWT 검사를 넣거나, 관리자 API에서 JWT 검사를 빼지 않는다.
