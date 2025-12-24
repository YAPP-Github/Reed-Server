-- 1. reading_records 테이블 수정
-- page_number를 nullable로 변경
ALTER TABLE reading_records MODIFY COLUMN page_number INT NULL;

-- primary_emotion 컬럼 추가
ALTER TABLE reading_records ADD COLUMN primary_emotion VARCHAR(20) NULL;

-- 기존 데이터 마이그레이션 (tags 기반으로 primary_emotion 설정)
UPDATE reading_records rr
SET primary_emotion = COALESCE(
    (SELECT CASE t.name
        WHEN '따뜻함' THEN 'WARMTH'
        WHEN '즐거움' THEN 'JOY'
        WHEN '슬픔' THEN 'SADNESS'
        WHEN '깨달음' THEN 'INSIGHT'
        ELSE 'OTHER'
    END
    FROM reading_record_tags rrt
    JOIN tags t ON rrt.tag_id = t.id
    WHERE rrt.reading_record_id = rr.id AND rrt.deleted_at IS NULL
    LIMIT 1),
    'OTHER'
);

-- NOT NULL 제약 추가
ALTER TABLE reading_records MODIFY COLUMN primary_emotion VARCHAR(20) NOT NULL;

-- 2. detail_tags 테이블 생성
CREATE TABLE detail_tags (
    id              VARCHAR(36) NOT NULL,
    created_at      datetime(6) NOT NULL,
    updated_at      datetime(6) NOT NULL,
    primary_emotion VARCHAR(20) NOT NULL,
    name            VARCHAR(20) NOT NULL,
    display_order   INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_detail_tags PRIMARY KEY (id),
    CONSTRAINT uq_detail_tags_emotion_name UNIQUE (primary_emotion, name)
);

-- 3. reading_record_detail_tags 테이블 생성
CREATE TABLE reading_record_detail_tags (
    id                  VARCHAR(36) NOT NULL,
    created_at          datetime(6) NOT NULL,
    updated_at          datetime(6) NOT NULL,
    deleted_at          datetime(6) NULL,
    reading_record_id   VARCHAR(36) NOT NULL,
    detail_tag_id       VARCHAR(36) NOT NULL,
    CONSTRAINT pk_reading_record_detail_tags PRIMARY KEY (id),
    CONSTRAINT uq_record_detail_tag UNIQUE (reading_record_id, detail_tag_id),
    CONSTRAINT fk_rrdt_reading_record FOREIGN KEY (reading_record_id) REFERENCES reading_records(id) ON DELETE CASCADE,
    CONSTRAINT fk_rrdt_detail_tag FOREIGN KEY (detail_tag_id) REFERENCES detail_tags(id)
);

CREATE INDEX idx_rrdt_reading_record_id ON reading_record_detail_tags(reading_record_id);
CREATE INDEX idx_rrdt_detail_tag_id ON reading_record_detail_tags(detail_tag_id);

-- 4. 세부감정 초기 데이터 삽입
INSERT INTO detail_tags (id, created_at, updated_at, primary_emotion, name, display_order) VALUES
-- 즐거움
(UUID(), NOW(), NOW(), 'JOY', '설레는', 1),
(UUID(), NOW(), NOW(), 'JOY', '뿌듯한', 2),
(UUID(), NOW(), NOW(), 'JOY', '유쾌한', 3),
(UUID(), NOW(), NOW(), 'JOY', '기쁜', 4),
(UUID(), NOW(), NOW(), 'JOY', '흥미진진한', 5),
-- 따뜻함
(UUID(), NOW(), NOW(), 'WARMTH', '위로받은', 1),
(UUID(), NOW(), NOW(), 'WARMTH', '포근한', 2),
(UUID(), NOW(), NOW(), 'WARMTH', '다정한', 3),
(UUID(), NOW(), NOW(), 'WARMTH', '고마운', 4),
(UUID(), NOW(), NOW(), 'WARMTH', '마음이 놓이는', 5),
(UUID(), NOW(), NOW(), 'WARMTH', '편안한', 6),
-- 슬픔
(UUID(), NOW(), NOW(), 'SADNESS', '허무한', 1),
(UUID(), NOW(), NOW(), 'SADNESS', '외로운', 2),
(UUID(), NOW(), NOW(), 'SADNESS', '아쉬운', 3),
(UUID(), NOW(), NOW(), 'SADNESS', '먹먹한', 4),
(UUID(), NOW(), NOW(), 'SADNESS', '애틋한', 5),
(UUID(), NOW(), NOW(), 'SADNESS', '안타까운', 6),
(UUID(), NOW(), NOW(), 'SADNESS', '그리운', 7),
-- 깨달음
(UUID(), NOW(), NOW(), 'INSIGHT', '감탄한', 1),
(UUID(), NOW(), NOW(), 'INSIGHT', '통찰력을 얻은', 2),
(UUID(), NOW(), NOW(), 'INSIGHT', '영감을 받은', 3),
(UUID(), NOW(), NOW(), 'INSIGHT', '생각이 깊어진', 4),
(UUID(), NOW(), NOW(), 'INSIGHT', '새롭게 이해한', 5);
