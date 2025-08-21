INSERT INTO tags (id, name, created_at, updated_at)
VALUES ('0198c238-dd86-737c-af79-8875fe2290c1', '깨달음', NOW(6), NOW(6)),
       ('0198c238-dd86-737c-af79-8875fe2290c2', '슬픔', NOW(6), NOW(6)),
       ('0198c238-dd86-737c-af79-8875fe2290c3', '즐거움', NOW(6), NOW(6)),
       ('0198c238-dd86-737c-af79-8875fe2290c4', '따뜻함', NOW(6), NOW(6))
ON DUPLICATE KEY UPDATE name       = VALUES(name),
                        updated_at = NOW(6);
