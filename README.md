# JobMatch API

취업 정보 추천·탐색 서비스의 백엔드 API입니다. 사용자 조건 기반 직업 추천, 직업훈련·고용지원제도 탐색, 관심 직업 저장·비교, 관리자 데이터 관리 기능을 제공합니다.

## 주요 기능

- **사용자 조건 입력**: 경력, 학력, 희망 근무형태, 관심분야 등록
- **직업 추천**: 가중치 기반 매칭 점수로 상위 직업 추천 (`docs/erd.md` 5번 섹션 참고)
- **정보 탐색**: 직업 / 직업훈련 과정 / 지역별 일자리 / 고용지원제도 조회
- **저장 및 비교**: 관심 직업·훈련 저장, 여러 직업 나란히 비교
- **관리자**: JWT 인증, 데이터 CRUD, 추천 가중치 조정

## 기술 스택

| 구분 | 내용 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.4 |
| ORM | Spring Data JPA (Hibernate) |
| DB | PostgreSQL 16 |
| 빌드 도구 | Maven |
| API 문서 | springdoc-openapi (Swagger UI) |
| 인증 | 일반 사용자: sessionKey(UUID) / 관리자: JWT |
| 컨테이너 | Docker, Docker Compose |
| 배포 | Railway |

## 프로젝트 구조

```
src/main/java/com/project/jobmatch/
├── domain/           # 도메인별 entity / repository / dto / service / controller
├── common/           # 공통 응답 포맷, 예외 처리, 설정
└── JobMatchApplication.java

docs/
├── erd.md            # 데이터베이스 설계 문서
└── api-spec.md        # API 명세

sql/
├── 01-schema.sql       # 테이블 생성 DDL
└── 02-seed.sql         # 개발용 샘플 데이터

AGENTS.md              # AI 코딩 에이전트(Codex 등)를 위한 개발 지침
```

## 로컬 실행 방법

### 준비물

- Docker Desktop
- Git

### 실행

```bash
git clone https://github.com/{계정}/jobmatch-api.git
cd jobmatch-api
cp .env.example .env
docker compose up -d --build
```

기동 후 아래 주소에서 확인할 수 있습니다.

- API 서버: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- DB 관리 화면(Adminer): http://localhost:8081

### 스키마 변경 시

`sql/01-schema.sql` 또는 `sql/02-seed.sql`을 수정한 경우, 볼륨을 새로 만들어야 반영됩니다.

```bash
docker compose down -v
docker compose up -d --build
```

> DB는 `ddl-auto: validate`로 설정되어 있어 스키마를 Hibernate가 자동 생성하지 않습니다. 반드시 `sql/01-schema.sql`을 먼저 반영해야 애플리케이션이 정상 기동됩니다.

## API 문서

전체 API 명세는 [`docs/api-spec.md`](docs/api-spec.md)에서 확인할 수 있고, 실행 중인 서버에서는 Swagger UI로 직접 호출해볼 수 있습니다.

관리자 API(`/api/v1/admin/**`)는 `POST /api/v1/admin/login`으로 JWT를 발급받아 Swagger UI 우측 상단 **Authorize**에 `Bearer {token}` 형식으로 입력해야 호출 가능합니다.

## 데이터베이스 설계

전체 ERD와 테이블 명세는 [`docs/erd.md`](docs/erd.md)를 참고하세요.

## 배포

Railway에 Dockerfile 기반으로 배포되어 있습니다. 배포 환경 변수는 다음을 사용합니다.

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

배포된 PostgreSQL에는 로컬과 동일하게 `sql/01-schema.sql`, `sql/02-seed.sql`을 최초 1회 수동으로 적용해야 합니다.

## 개발 방식

이 프로젝트는 [Codex CLI](https://github.com/openai/codex)를 활용해 개발되었습니다. `AGENTS.md`에 개발 순서, 코딩 컨벤션, 완료 기준이 정의되어 있으며, Codex가 저장소 루트의 이 파일을 자동으로 읽습니다. 새로운 기능을 추가할 때도 `AGENTS.md`의 구현 순서(Entity → Repository → DTO → Service → Controller)를 따르는 것을 권장합니다.

## CORS

현재 모든 origin(`*`)을 허용하도록 설정되어 있습니다. 프론트엔드 배포 주소가 확정되면 특정 origin으로 제한하는 것을 권장합니다.
