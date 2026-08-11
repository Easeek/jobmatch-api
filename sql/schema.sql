CREATE TABLE region (
    region_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    region_code VARCHAR(20) NOT NULL UNIQUE,
    region_name VARCHAR(50) NOT NULL
);

CREATE TABLE interest_field (
    field_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    field_code VARCHAR(30) NOT NULL UNIQUE,
    field_name VARCHAR(50) NOT NULL
);

CREATE TABLE "user" (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE user_condition (
    condition_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    session_key VARCHAR(64) NOT NULL,
    user_id BIGINT REFERENCES "user"(user_id),
    career_level VARCHAR(20) NOT NULL,
    region_id BIGINT REFERENCES region(region_id),
    work_type VARCHAR(20),
    education_level VARCHAR(20) NOT NULL,
    training_desired BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_condition_session_key ON user_condition(session_key);

CREATE TABLE user_condition_interest_field (
    condition_id BIGINT NOT NULL REFERENCES user_condition(condition_id),
    field_id BIGINT NOT NULL REFERENCES interest_field(field_id),
    PRIMARY KEY (condition_id, field_id)
);

CREATE TABLE job (
    job_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    job_code VARCHAR(30) NOT NULL UNIQUE,
    job_name VARCHAR(50) NOT NULL,
    required_education VARCHAR(20),
    avg_salary_text VARCHAR(100),
    description TEXT,
    source VARCHAR(30),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE job_interest_field (
    job_id BIGINT NOT NULL REFERENCES job(job_id),
    field_id BIGINT NOT NULL REFERENCES interest_field(field_id),
    PRIMARY KEY (job_id, field_id)
);

CREATE TABLE job_work_type (
    job_id BIGINT NOT NULL REFERENCES job(job_id),
    work_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (job_id, work_type)
);

CREATE TABLE training_course (
    course_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    institution VARCHAR(100),
    region_id BIGINT REFERENCES region(region_id),
    cost_type VARCHAR(20),
    start_date DATE,
    end_date DATE,
    description TEXT,
    external_url VARCHAR(500),
    source VARCHAR(30),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE support_program (
    program_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    program_name VARCHAR(200) NOT NULL,
    organization VARCHAR(100),
    target_audience VARCHAR(200),
    support_content TEXT,
    region_id BIGINT REFERENCES region(region_id),
    apply_start_date DATE,
    apply_end_date DATE,
    external_url VARCHAR(500),
    source VARCHAR(30),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE job_posting (
    posting_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    job_id BIGINT REFERENCES job(job_id),
    title VARCHAR(200) NOT NULL,
    company_name VARCHAR(100),
    region_id BIGINT REFERENCES region(region_id),
    work_type VARCHAR(20),
    required_education VARCHAR(20),
    career_level VARCHAR(20),
    salary_text VARCHAR(100),
    apply_deadline DATE,
    external_url VARCHAR(500),
    source VARCHAR(30),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE job_training_course (
    job_id BIGINT NOT NULL REFERENCES job(job_id),
    course_id BIGINT NOT NULL REFERENCES training_course(course_id),
    PRIMARY KEY (job_id, course_id)
);

CREATE TABLE job_support_program (
    job_id BIGINT NOT NULL REFERENCES job(job_id),
    program_id BIGINT NOT NULL REFERENCES support_program(program_id),
    PRIMARY KEY (job_id, program_id)
);
