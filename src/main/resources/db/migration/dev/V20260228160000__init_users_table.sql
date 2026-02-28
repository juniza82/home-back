-- users 테이블 생성 및 기본 스키마 정의 (2026-02-28 초기화)
CREATE TABLE users
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(12) NOT NULL,
    birthday        DATE,
    joindate        DATE,
    quitdate        DATE,
    team            VARCHAR(12),
    position        VARCHAR(12),
    positionjob     VARCHAR(12),
    memo            VARCHAR(12),
    email           VARCHAR(50) NOT NULL,
    test            VARCHAR(100),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN users.id IS 'ID';
COMMENT ON COLUMN users.name IS '이름';
COMMENT ON COLUMN users.birthday IS '생년월일';
COMMENT ON COLUMN users.joindate IS '입사일';
COMMENT ON COLUMN users.quitdate IS '퇴사일';
COMMENT ON COLUMN users.team IS '소속팀';
COMMENT ON COLUMN users.position IS '직책';
COMMENT ON COLUMN users.positionjob IS '직무';
COMMENT ON COLUMN users.memo IS '메모';
COMMENT ON COLUMN users.email IS '이메일';
COMMENT ON COLUMN users.test IS '테스트 컬럼';
COMMENT ON COLUMN users.created_at IS '생성일시';
COMMENT ON COLUMN users.updated_at IS '수정일시';
