ALTER TABLE device
ADD COLUMN deleted_at DATETIME(6) NULL COMMENT '삭제 시간';

ALTER TABLE notification
ADD COLUMN deleted_at DATETIME(6) NULL COMMENT '삭제 시간';
