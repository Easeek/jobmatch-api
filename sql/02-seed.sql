INSERT INTO region (region_code, region_name) VALUES
    ('NATIONWIDE', '전국'),
    ('SEOUL', '서울특별시'),
    ('BUSAN', '부산광역시'),
    ('DAEGU', '대구광역시'),
    ('INCHEON', '인천광역시'),
    ('GWANGJU', '광주광역시'),
    ('DAEJEON', '대전광역시'),
    ('ULSAN', '울산광역시'),
    ('SEJONG', '세종특별자치시'),
    ('GYEONGGI', '경기도'),
    ('GANGWON', '강원특별자치도'),
    ('CHUNGBUK', '충청북도'),
    ('CHUNGNAM', '충청남도'),
    ('JEONBUK', '전북특별자치도'),
    ('JEONNAM', '전라남도'),
    ('GYEONGBUK', '경상북도'),
    ('GYEONGNAM', '경상남도'),
    ('JEJU', '제주특별자치도');

INSERT INTO interest_field (field_code, field_name) VALUES
    ('IT', 'IT·소프트웨어'),
    ('MARKETING', '마케팅·광고'),
    ('DESIGN', '디자인'),
    ('HR', '인사·노무'),
    ('MANUFACTURING', '제조·생산'),
    ('HEALTHCARE', '보건·의료');

-- Development administrator: admin@jobmatch.local / admin1234
INSERT INTO "user" (email, password, name, role, created_at, updated_at) VALUES
    ('admin@jobmatch.local', '$2a$10$WI3h7Ztmy31RqDPSP8qNO.y.nqm6DoZysVEzyxydxAoasl46flM/S',
     '개발 관리자', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO job (
    job_code, job_name, required_education, avg_salary_text, description, source, created_at, updated_at
) VALUES
    ('BACKEND_DEV', '백엔드 개발자', 'BACHELOR', '신입 연 3,400만원~',
     '서버 애플리케이션과 API를 설계하고 개발합니다.', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('DIGITAL_MARKETER', '디지털 마케터', 'BACHELOR', '신입 연 3,000만원~',
     '온라인 캠페인을 기획하고 성과 데이터를 분석합니다.', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('UX_UI_DESIGNER', 'UX/UI 디자이너', 'ASSOCIATE', '신입 연 3,100만원~',
     '사용자 경험을 분석하고 웹·앱 인터페이스를 설계합니다.', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HR_MANAGER', '인사 담당자', 'BACHELOR', '신입 연 3,000만원~',
     '채용, 평가, 교육 등 조직의 인사 업무를 수행합니다.', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SMART_FACTORY_ENGINEER', '스마트팩토리 엔지니어', 'ASSOCIATE', '신입 연 3,300만원~',
     '제조 설비 자동화와 생산 데이터 시스템을 운영합니다.', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO job_interest_field (job_id, field_id)
SELECT j.job_id, f.field_id
FROM (VALUES
    ('BACKEND_DEV', 'IT'),
    ('DIGITAL_MARKETER', 'MARKETING'),
    ('UX_UI_DESIGNER', 'DESIGN'),
    ('UX_UI_DESIGNER', 'IT'),
    ('HR_MANAGER', 'HR'),
    ('SMART_FACTORY_ENGINEER', 'MANUFACTURING'),
    ('SMART_FACTORY_ENGINEER', 'IT')
) AS mapping(job_code, field_code)
JOIN job j ON j.job_code = mapping.job_code
JOIN interest_field f ON f.field_code = mapping.field_code;

INSERT INTO job_work_type (job_id, work_type)
SELECT j.job_id, mapping.work_type
FROM (VALUES
    ('BACKEND_DEV', 'FULL_TIME'),
    ('BACKEND_DEV', 'REMOTE'),
    ('DIGITAL_MARKETER', 'FULL_TIME'),
    ('DIGITAL_MARKETER', 'CONTRACT'),
    ('UX_UI_DESIGNER', 'FULL_TIME'),
    ('UX_UI_DESIGNER', 'REMOTE'),
    ('HR_MANAGER', 'FULL_TIME'),
    ('SMART_FACTORY_ENGINEER', 'FULL_TIME')
) AS mapping(job_code, work_type)
JOIN job j ON j.job_code = mapping.job_code;

INSERT INTO training_course (
    course_name, institution, region_id, cost_type, start_date, end_date,
    description, external_url, source, created_at, updated_at
) VALUES
    ('국비 백엔드 개발자 양성과정', '서울디지털아카데미',
     (SELECT region_id FROM region WHERE region_code = 'SEOUL'), 'GOV_SUPPORTED',
     CURRENT_DATE + 30, CURRENT_DATE + 210, 'Java와 Spring Boot 기반 백엔드 실무 과정입니다.',
     'https://example.com/trainings/backend', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('데이터 기반 디지털 마케팅', '부산마케팅교육원',
     (SELECT region_id FROM region WHERE region_code = 'BUSAN'), 'FREE',
     CURRENT_DATE + 20, CURRENT_DATE + 80, '광고 데이터 분석과 캠페인 운영을 학습합니다.',
     'https://example.com/trainings/marketing', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('UX/UI 포트폴리오 부트캠프', '경기디자인센터',
     (SELECT region_id FROM region WHERE region_code = 'GYEONGGI'), 'PAID',
     CURRENT_DATE + 45, CURRENT_DATE + 135, '사용자 조사부터 프로토타입 제작까지 진행합니다.',
     'https://example.com/trainings/uxui', 'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO support_program (
    program_name, organization, target_audience, support_content, region_id,
    apply_start_date, apply_end_date, external_url, source, created_at, updated_at
) VALUES
    ('국민취업지원제도', '고용노동부', '취업을 원하는 구직자',
     '취업지원 서비스와 요건 충족 시 구직촉진수당을 제공합니다.', NULL,
     CURRENT_DATE, CURRENT_DATE + 365, 'https://example.com/support/employment',
     'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('서울 청년취업사관학교', '서울특별시', '서울 거주 청년 구직자',
     '디지털 실무 교육과 취업 연계를 지원합니다.',
     (SELECT region_id FROM region WHERE region_code = 'SEOUL'),
     CURRENT_DATE, CURRENT_DATE + 180, 'https://example.com/support/seoul-youth',
     'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('경기 청년 면접수당', '경기도', '경기도 거주 청년 구직자',
     '구직 활동을 위한 면접 비용을 지원합니다.',
     (SELECT region_id FROM region WHERE region_code = 'GYEONGGI'),
     CURRENT_DATE, CURRENT_DATE + 120, 'https://example.com/support/gyeonggi-interview',
     'SAMPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO job_training_course (job_id, course_id)
SELECT j.job_id, t.course_id
FROM (VALUES
    ('BACKEND_DEV', '국비 백엔드 개발자 양성과정'),
    ('DIGITAL_MARKETER', '데이터 기반 디지털 마케팅'),
    ('UX_UI_DESIGNER', 'UX/UI 포트폴리오 부트캠프')
) AS mapping(job_code, course_name)
JOIN job j ON j.job_code = mapping.job_code
JOIN training_course t ON t.course_name = mapping.course_name;

INSERT INTO job_support_program (job_id, program_id)
SELECT j.job_id, p.program_id
FROM job j
CROSS JOIN support_program p
WHERE p.program_name = '국민취업지원제도';

INSERT INTO job_support_program (job_id, program_id)
SELECT j.job_id, p.program_id
FROM (VALUES
    ('BACKEND_DEV', '서울 청년취업사관학교'),
    ('UX_UI_DESIGNER', '경기 청년 면접수당')
) AS mapping(job_code, program_name)
JOIN job j ON j.job_code = mapping.job_code
JOIN support_program p ON p.program_name = mapping.program_name;

INSERT INTO recommendation_criteria (criteria_key, weight, is_active, description, updated_at) VALUES
    ('INTEREST_FIELD_MATCH', 30.00, TRUE, '관심분야 일치 점수', CURRENT_TIMESTAMP),
    ('WORK_TYPE_MATCH', 20.00, TRUE, '희망 근무형태 일치 점수', CURRENT_TIMESTAMP),
    ('EDUCATION_MATCH', 20.00, TRUE, '학력 충족 점수', CURRENT_TIMESTAMP),
    ('CAREER_MATCH', 15.00, TRUE, '경력 수준 적합 점수', CURRENT_TIMESTAMP),
    ('REGION_MATCH', 15.00, TRUE, '선택 지역 채용공고 존재 점수', CURRENT_TIMESTAMP);
