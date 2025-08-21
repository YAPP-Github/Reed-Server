CREATE TABLE users
(
    id                  VARCHAR(36)   NOT NULL,
    created_at          datetime(6)   NOT NULL,
    updated_at          datetime(6)   NOT NULL,
    deleted_at          datetime(6)      NULL,
    email               VARCHAR(100)  NOT NULL,
    provider_type       ENUM ('APPLE', 'KAKAO') NOT NULL,
    provider_id         VARCHAR(100)  NOT NULL,
    nickname            VARCHAR(100)  NOT NULL,
    profile_image_url   VARCHAR(255)  NULL,
    `role`              ENUM ('ADMIN', 'USER')  NOT NULL,
    terms_agreed        BIT(1)        NOT NULL,
    apple_refresh_token VARCHAR(1024) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE books
(
    id               VARCHAR(36)   NOT NULL,
    created_at       datetime(6)   NOT NULL,
    updated_at       datetime(6)   NOT NULL,
    deleted_at       datetime(6)      NULL,
    isbn13           VARCHAR(13)   NOT NULL,
    title            VARCHAR(255)  NOT NULL,
    author           VARCHAR(255)  NULL,
    publisher        VARCHAR(300)  NULL,
    publication_year INT           NULL,
    cover_image_url  VARCHAR(2048) NULL,
    `description`    VARCHAR(2000) NULL,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE user_books
(
    id                   VARCHAR(36)   NOT NULL,
    created_at           datetime(6)   NOT NULL,
    updated_at           datetime(6)   NOT NULL,
    deleted_at           datetime(6)     NULL,
    user_id              VARCHAR(36)   NOT NULL,
    book_id              VARCHAR(36)   NOT NULL,
    book_isbn13          VARCHAR(255)  NOT NULL,
    cover_image_url      VARCHAR(2048) NOT NULL,
    publisher            VARCHAR(300)  NOT NULL,
    title                VARCHAR(255)  NOT NULL,
    author               VARCHAR(255)  NULL,
    `status`             ENUM ('BEFORE_READING', 'BEFORE_REGISTRATION', 'COMPLETED', 'READING') NOT NULL,
    reading_record_count INT           NOT NULL,
    CONSTRAINT pk_user_books PRIMARY KEY (id)
);

CREATE TABLE reading_records
(
    id           VARCHAR(36)       NOT NULL,
    created_at   datetime(6)   NOT NULL,
    updated_at   datetime(6)   NOT NULL,
    deleted_at   datetime(6)      NULL,
    user_book_id VARCHAR(36)   NOT NULL,
    page_number  INT           NOT NULL,
    quote        VARCHAR(1000) NOT NULL,
    review       VARCHAR(1000) NOT NULL,
    CONSTRAINT pk_reading_records PRIMARY KEY (id)
);

CREATE TABLE tags
(
    id         VARCHAR(36)     NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)    NULL,
    name       VARCHAR(10) NOT NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

CREATE TABLE reading_record_tags
(
    id                VARCHAR(36)     NOT NULL,
    created_at        datetime(6) NOT NULL,
    updated_at        datetime(6) NOT NULL,
    deleted_at        datetime(6)    NULL,
    reading_record_id VARCHAR(36) NOT NULL,
    tag_id            VARCHAR(36) NOT NULL,
    CONSTRAINT pk_reading_record_tags PRIMARY KEY (id)
);

ALTER TABLE books
    ADD CONSTRAINT uc_books_isbn13 UNIQUE (isbn13);

ALTER TABLE tags
    ADD CONSTRAINT uc_tags_name UNIQUE (name);

CREATE INDEX idx_user_books_title ON user_books (title);

CREATE INDEX idx_user_books_user_id_title ON user_books (user_id, title);
