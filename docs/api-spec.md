# 취업 정보 추천·탐색 서비스 — API 명세

> 초기 기획서 5개 핵심 기능 + 4주차/5주차 개발 항목 전체 반영.

---

## 0. 공통 사항

- **Base Path**: `/api/v1`
- **인증**:
  - 일반 사용자 기능(조건/추천/저장)은 **비로그인** — 요청에 `sessionKey`(UUID) 포함.
  - 관리자 기능(`/admin/**`)은 **JWT 필요**(`Authorization: Bearer {token}`, `role=ADMIN`).
- **공통 응답 포맷**
```json
{
  "success": true,
  "data": { },
  "error": null
}
```
- **에러 응답 포맷**
```json
{
  "success": false,
  "data": null,
  "error": { "code": "JOB_NOT_FOUND", "message": "해당 직업을 찾을 수 없습니다." }
}
```
- 이하 명세에서는 `data` 내부 구조만 기술한다.

---

## 1. 사용자 조건 입력 API (기능 1 · 4주차)

**POST `/conditions`** — 사용자 조건 저장

Request
```json
{
  "sessionKey": "uuid-string",
  "careerLevel": "NEW",
  "regionId": 1,
  "workType": "FULL_TIME",
  "educationLevel": "BACHELOR",
  "trainingDesired": true,
  "interestFieldIds": [1, 3]
}
```
Response
```json
{ "conditionId": 100, "createdAt": "2026-08-01T10:00:00" }
```

**GET `/conditions/{conditionId}`** — 저장된 조건 조회

Response
```json
{
  "conditionId": 100,
  "sessionKey": "uuid-string",
  "careerLevel": "NEW",
  "region": { "regionId": 1, "regionName": "서울특별시" },
  "workType": "FULL_TIME",
  "educationLevel": "BACHELOR",
  "trainingDesired": true,
  "interestFields": [
    { "fieldId": 1, "fieldName": "IT" },
    { "fieldId": 3, "fieldName": "마케팅" }
  ]
}
```

**GET `/meta/conditions`** — 조건 입력용 코드값 목록 (드롭다운 채우기)

프론트가 지역/관심분야/근무형태 등 선택지를 한 번에 받도록 제공.

Response
```json
{
  "regions": [ { "regionId": 1, "regionCode": "SEOUL", "regionName": "서울특별시" } ],
  "interestFields": [ { "fieldId": 1, "fieldCode": "IT", "fieldName": "IT" } ],
  "careerLevels": ["NEW", "JUNIOR", "MID", "SENIOR"],
  "workTypes": ["FULL_TIME", "PART_TIME", "CONTRACT", "INTERN", "REMOTE"],
  "educationLevels": ["HIGH_SCHOOL", "ASSOCIATE", "BACHELOR", "MASTER", "DOCTORATE"]
}
```

---

## 2. 직업/고용 정보 추천 API (기능 2 · 4주차)

**POST `/conditions/{conditionId}/recommendations`** — 추천 실행

저장된 조건으로 추천 로직을 실행하고 결과를 생성.

Response
```json
{
  "resultId": 500,
  "conditionId": 100,
  "recommendedCount": 5,
  "items": [
    {
      "itemId": 1,
      "rankOrder": 1,
      "job": { "jobId": 10, "jobName": "백엔드 개발자" },
      "matchScore": 87.5,
      "reason": "관심분야(IT)와 희망 근무형태(정규직)가 일치하며, 서울 지역 채용공고가 다수 존재합니다.",
      "relatedTrainingCount": 3,
      "relatedSupportCount": 2
    }
  ]
}
```

**GET `/recommendations/{resultId}`** — 추천 결과 재조회

응답 구조는 위와 동일.

**GET `/recommendations/{resultId}/items/{itemId}`** — 추천 직업 상세 (연관 정보 포함)

추천 이유 + **관련 직업훈련·고용지원제도 연결** 정보 반환.

Response
```json
{
  "itemId": 1,
  "job": {
    "jobId": 10,
    "jobName": "백엔드 개발자",
    "description": "...",
    "requiredEducation": "BACHELOR",
    "avgSalaryText": "신입 연 3,400만원~"
  },
  "matchScore": 87.5,
  "reason": "...",
  "relatedTrainings": [
    { "courseId": 21, "courseName": "국비 백엔드 양성과정", "institution": "...", "costType": "GOV_SUPPORTED" }
  ],
  "relatedSupportPrograms": [
    { "programId": 5, "programName": "청년내일채움공제", "organization": "고용노동부" }
  ]
}
```

---

## 3. 공공데이터 기반 정보 탐색 API (기능 3 · 4주차)

### 3-1. 직업 정보

**GET `/jobs`** — 직업 목록

Query: `fieldId`, `keyword` (optional)

Response
```json
[ { "jobId": 10, "jobName": "백엔드 개발자", "fields": ["IT"] } ]
```

**GET `/jobs/{jobId}`** — 직업 상세

Response: 직업 정보 + 관심분야 + 연관 훈련/지원제도/근무형태 요약.

### 3-2. 직업훈련 과정

**GET `/trainings`** — 훈련 과정 목록

Query: `regionId`, `costType`, `jobId`, `keyword` (optional)

Response
```json
[
  {
    "courseId": 21,
    "courseName": "국비 백엔드 양성과정",
    "institution": "OO아카데미",
    "region": "서울",
    "costType": "GOV_SUPPORTED",
    "startDate": "2026-09-01",
    "endDate": "2027-02-28"
  }
]
```

**GET `/trainings/{courseId}`** — 훈련 과정 상세 (설명·URL 포함)

### 3-3. 지역별 일자리 정보

**GET `/postings`** — 일자리 목록

Query: `regionId`, `jobId`, `workType`, `keyword` (optional), `sort`(`deadline`/`latest`)

Response
```json
[
  {
    "postingId": 300,
    "title": "백엔드 신입 채용",
    "companyName": "OO소프트",
    "region": "서울",
    "workType": "FULL_TIME",
    "careerLevel": "NEW",
    "applyDeadline": "2026-08-31"
  }
]
```

**GET `/postings/{postingId}`** — 일자리 상세

### 3-4. 고용지원제도 정보

**GET `/support-programs`** — 지원제도 목록

Query: `regionId`, `jobId`, `keyword` (optional)

Response
```json
[
  {
    "programId": 5,
    "programName": "청년내일채움공제",
    "organization": "고용노동부",
    "targetAudience": "만 15~34세 청년",
    "region": "전국",
    "applyEndDate": "2026-12-31"
  }
]
```

**GET `/support-programs/{programId}`** — 지원제도 상세

---

## 4. 결과 저장 및 비교 API (기능 4 · 5주차)

### 4-1. 관심 직업 저장

**POST `/saved-jobs`** — 관심 직업 저장

Request
```json
{ "sessionKey": "uuid-string", "jobId": 10, "memo": "1순위 목표" }
```
Response: `{ "savedJobId": 700, "createdAt": "..." }`

중복 저장 시 409(`ALREADY_SAVED`).

**GET `/saved-jobs?sessionKey={key}`** — 관심 직업 목록 조회

**DELETE `/saved-jobs/{savedJobId}`** — 관심 직업 삭제

### 4-2. 관심 훈련 과정 저장

**POST `/saved-trainings`** — 관심 훈련 저장

Request: `{ "sessionKey": "uuid-string", "courseId": 21 }`

Response: `{ "savedTrainingId": 800, "createdAt": "..." }`

**GET `/saved-trainings?sessionKey={key}`** — 관심 훈련 목록 조회

**DELETE `/saved-trainings/{savedTrainingId}`** — 관심 훈련 삭제

### 4-3. 추천 결과 비교

**GET `/jobs/compare`** — 직업 비교 (저장 없이 즉시 비교)

Query: `jobIds=10,15,22` (콤마 구분, 2~4개 권장)

직업들을 항목별로 나란히 반환해 프론트가 표로 렌더링.

Response
```json
{
  "jobs": [
    {
      "jobId": 10,
      "jobName": "백엔드 개발자",
      "requiredEducation": "BACHELOR",
      "avgSalaryText": "신입 연 3,400만원~",
      "fields": ["IT"],
      "trainingCount": 3,
      "supportCount": 2,
      "postingCount": 15
    },
    {
      "jobId": 15,
      "jobName": "데이터 분석가",
      "requiredEducation": "BACHELOR",
      "avgSalaryText": "신입 연 3,600만원~",
      "fields": ["IT"],
      "trainingCount": 2,
      "supportCount": 1,
      "postingCount": 9
    }
  ]
}
```

> 관심 직업(`saved_jobs`)에 담긴 직업들만 비교하려면 프론트가 저장 목록에서 `jobIds`를 뽑아 이 API를 호출한다.

---

## 5. 관리자 / 데이터 관리 API (기능 5)

> 모두 `role=ADMIN` JWT 필요. Base: `/api/v1/admin`

### 5-1. 관리자 로그인

**POST `/admin/login`**

Request: `{ "email": "admin@...", "password": "..." }`

Response: `{ "accessToken": "jwt..." }`

### 5-2. 데이터 관리 (직업/훈련/지원제도 CRUD)

| 대상 | 메서드 & 경로 |
|---|---|
| 직업 | `POST /admin/jobs`, `PUT /admin/jobs/{jobId}`, `DELETE /admin/jobs/{jobId}` |
| 훈련 과정 | `POST /admin/trainings`, `PUT /admin/trainings/{courseId}`, `DELETE /admin/trainings/{courseId}` |
| 지원제도 | `POST /admin/support-programs`, `PUT /admin/support-programs/{programId}`, `DELETE /admin/support-programs/{programId}` |
| 일자리 | `POST /admin/postings`, `PUT /admin/postings/{postingId}`, `DELETE /admin/postings/{postingId}` |
| 연관 매핑 | `POST /admin/jobs/{jobId}/trainings`, `POST /admin/jobs/{jobId}/support-programs` (연결 등록) |

예) **POST `/admin/jobs`** Request
```json
{
  "jobCode": "BACKEND_DEV",
  "jobName": "백엔드 개발자",
  "requiredEducation": "BACHELOR",
  "description": "...",
  "interestFieldIds": [1],
  "workTypes": ["FULL_TIME", "REMOTE"]
}
```

### 5-3. 공공데이터 수집/갱신

**POST `/admin/data-sync`** — 공공데이터 동기화 실행

Request: `{ "sourceType": "HRD_NET", "targetTable": "training_course" }`

Response: `{ "syncId": 900, "status": "RUNNING" }`

**GET `/admin/data-sync/logs`** — 동기화 이력 조회

Query: `sourceType`, `status` (optional)

Response
```json
[
  {
    "syncId": 900,
    "sourceType": "HRD_NET",
    "targetTable": "training_course",
    "status": "SUCCESS",
    "syncedCount": 152,
    "startedAt": "...",
    "finishedAt": "..."
  }
]
```

### 5-4. 추천 기준 관리

**GET `/admin/recommendation-criteria`** — 추천 기준(가중치) 목록

**PUT `/admin/recommendation-criteria/{criteriaId}`** — 가중치/활성화 수정

Request: `{ "weight": 30.0, "isActive": true }`

저장 후 다음 추천 실행부터 즉시 반영.

---

## 6. 개발 순서 & 주차 매핑

### 4주차 (핵심 기능)

| 산출물 | 관련 API |
|---|---|
| 조건 입력 기능 | `POST /conditions`, `GET /meta/conditions` |
| 직업/훈련/지원제도 조회 | `GET /jobs`, `GET /trainings`, `GET /postings`, `GET /support-programs` |
| 추천 API + 추천 로직 1차 | `POST /conditions/{id}/recommendations`, `GET /recommendations/{id}` |
| 추천 결과 상세(연관 정보) | `GET /recommendations/{id}/items/{itemId}` |

### 5주차 (저장/비교 · 데이터 보완)

| 산출물 | 관련 API |
|---|---|
| 관심 직업 저장/삭제 | `POST /saved-jobs`, `GET /saved-jobs`, `DELETE /saved-jobs/{id}` |
| 관심 훈련 저장/삭제 | `POST /saved-trainings`, `GET /saved-trainings`, `DELETE /saved-trainings/{id}` |
| 추천 결과 비교 | `GET /jobs/compare` |
| 예외 처리/오류 메시지 정리 | 공통 에러 응답 포맷 적용, 404/409 처리 |

### 구현 순서 제안 (레이어 순서: Entity → Repository → DTO → Service → Controller)

1. **마스터 데이터 확정 & 시딩**: `region`, `interest_field`, `job`, `training_course`, `support_program`, 매핑 테이블 → 시딩 데이터가 있어야 조회·추천이 동작.
2. **조건 입력**(`user_condition`) → 조회 API.
3. **추천 로직**(`recommendation_*` + `recommendation_criteria`) — 서비스의 핵심.
4. **정보 탐색 조회 API** — 마스터 데이터 기반 CRUD 성격, 병렬 진행 가능.
5. **저장/비교** — 상대적으로 단순, 5주차 배치.
6. **관리자 API** — 최소 로그인 + 데이터 CRUD + 추천 기준 관리부터, 공공데이터 동기화는 여력 되는 만큼.
