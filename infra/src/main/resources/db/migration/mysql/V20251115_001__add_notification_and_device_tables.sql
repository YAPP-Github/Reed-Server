-- Add notification_enabled and last_activity columns to users table
ALTER TABLE users
    ADD COLUMN notification_enabled BOOLEAN NOT NULL DEFAULT TRUE COMMENT '알림 수신 동의 여부',
    ADD COLUMN last_activity DATETIME(6) NULL COMMENT '마지막 활동 시간';

-- Create device table for multi-device push notification support
CREATE TABLE device
(
    id         VARCHAR(36)  NOT NULL COMMENT '디바이스 ID',
    created_at DATETIME(6)  NOT NULL COMMENT '생성 시간',
    updated_at DATETIME(6)  NOT NULL COMMENT '수정 시간',
    user_id    VARCHAR(36)  NOT NULL COMMENT '사용자 ID',
    device_id  VARCHAR(255) NOT NULL COMMENT '디바이스 고유 ID',
    fcm_token  VARCHAR(255) NOT NULL COMMENT 'FCM 토큰',
    CONSTRAINT pk_device PRIMARY KEY (id)
) COMMENT '사용자 디바이스 정보';

-- Create notification table
CREATE TABLE notification
(
    id                VARCHAR(36)                     NOT NULL COMMENT '알림 ID',
    created_at        DATETIME(6)                     NOT NULL COMMENT '생성 시간',
    updated_at        DATETIME(6)                     NOT NULL COMMENT '수정 시간',
    user_id           VARCHAR(36)                     NOT NULL COMMENT '사용자 ID',
    title             VARCHAR(255)                    NOT NULL COMMENT '알림 제목',
    message           VARCHAR(1000)                   NOT NULL COMMENT '알림 메시지',
    notification_type ENUM ('UNRECORDED', 'DORMANT') NOT NULL COMMENT '알림 타입',
    is_read           BOOLEAN                         NOT NULL DEFAULT FALSE COMMENT '읽음 여부',
    is_sent           BOOLEAN                         NOT NULL DEFAULT FALSE COMMENT '전송 여부',
    sent_at           DATETIME(6)                     NULL COMMENT '전송 시간',
    CONSTRAINT pk_notification PRIMARY KEY (id)
) COMMENT '사용자 알림 정보';

-- Create indexes for actual query usage only
CREATE INDEX idx_device_user_id ON device (user_id);
CREATE INDEX idx_device_device_id ON device (device_id);
CREATE INDEX idx_device_fcm_token ON device (fcm_token);

CREATE INDEX idx_notification_user_id ON notification (user_id);
CREATE INDEX idx_notification_is_sent ON notification (is_sent);

-- Composite index for: WHERE last_activity < ? AND notification_enabled = true
CREATE INDEX idx_users_last_activity_notification ON users (last_activity, notification_enabled);
