# Admin Modules (expected)

## 🔑 Key Role

- **관리자 전용 API**: ADMIN 권한을 가진 사용자만 접근 가능한 시스템 관리 기능
- **사용자 관리**: 전체 사용자 조회, 상태 변경, 계정 차단/해제, 비밀번호 초기화
- **시스템 모니터링**: 대시보드, 통계 조회, 시스템 상태 확인
- **보안 관리**: 의심스러운 활동 감지, IP 차단, 세션 관리
- **컨텐츠 관리**: 공지사항 작성, 신고 처리, 로그 조회
- **관리자 전용 서비스**: 관리자 정책 적용, 권한 검증, 관리자 알림 처리

## ⛓️‍💥 Dependency Rule

```bash
Admin ─────→ Domain ─────→ Global-utils
  │            │
  │            └─────→ External Libraries (JPA, Database)
  │
  ├─────→ Infra ──────→ Domain
  │          │
  │          ├─────→ Global-utils
  │          │
  │          └─────→ External Libraries (Spring Data JPA, Redis, Kafka, AWS SDK)
  │
  ├─────→ Global-utils
  │
  └─────→ External Libraries (Spring Web, Validation, Security, etc.)
```

### ✅ Permitted **Dependency** Rule

- **Admin → Domain**: 공통 비즈니스 로직 호출 (UserDomainService, BookDomainService 등)
- **Admin → Infra**: InfraBaseConfigImportSelector를 통한 필요한 Infrastructure 설정 선택
- **Admin → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 검증 등)
- **Admin → External Libraries**: Spring Web, Validation 등

### ❌ Forbidden Dependency Rule

- **Admin → Gateway**: Admin은 Gateway의 JWT 기능을 사용하지 않음
- **Admin → APIs**: 같은 계층끼리는 서로 의존하지 않음
- **Admin → Batch**: Admin은 Batch 작업을 직접 호출하지 않음
- **Gateway → Admin**: Gateway는 Admin 로직을 알면 안 됨
- **APIs → Admin**: API 모듈이 관리자 모듈에 의존하지 않음
- **Batch → Admin**: Batch는 관리자 요청 처리와 무관
- **Domain → Admin**: 도메인은 Admin을 모르는 순수 영역
- **Infra → Admin**: 인프라는 관리 로직을 모름
- **Global-utils → Admin**: 공통 유틸리티는 Admin에 의존하지 않음
