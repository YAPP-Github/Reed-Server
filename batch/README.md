# Batch Modules (expected)

## 🔑 Key Role

- **스케줄링 작업**: 정해진 시간에 자동으로 실행되는 백그라운드 작업 처리
- **대량 데이터 처리**: 많은 양의 데이터를 효율적으로 일괄 처리
- **정기 업무**: 일일, 주간, 월간 반복 작업 (통계 생성, 데이터 정리, 알림 발송)
- **시스템 유지보수**: 로그 정리, 임시 파일 삭제, 데이터베이스 최적화
- **비동기 처리**: 시간이 오래 걸리는 작업을 백그라운드에서 처리
- **데이터 동기화**: 외부 시스템과의 데이터 동기화 및 마이그레이션

## ⛓️‍💥 Dependency Rule

```bash
Batch ─────→ Domain ─────→ Global-utils
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
  └─────→ External Libraries (Spring Batch, Scheduler, File Processing, etc.)
```

### ✅ Permitted **Dependency** Rule

- **Batch → Domain**: 공통 비즈니스 로직 호출 (UserDomainService, BookDomainService 등)
- **Batch → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 파일 처리 등)
- **Batch → Infra**: InfraBaseConfigImportSelector를 통한 필요한 Infrastructure 설정 선택
- **Batch → External Libraries**: Spring Batch, Scheduler, File Processing 등

### ❌ Forbidden Dependency Rule

- **Batch → Gateway**: Gateway는 HTTP 요청용, Batch는 스케줄링 작업용으로 무관
- **Batch → Admin**: Admin은 관리자 요청 처리용, Batch는 시스템 자동 작업
- **Batch → APIs**: APIs는 사용자 요청 처리용, Batch는 자동 실행되므로 의존 불필요
- **Gateway → Batch**: Gateway는 Batch 스케줄링을 알 필요 없음
- **Admin → Batch**: Admin은 Batch 작업을 직접 호출하지 않음
- **APIs → Batch**: APIs는 Batch 작업을 직접 호출하지 않음
- **Domain → Batch**: 도메인은 Batch를 모르는 순수 영역
- **Infra → Batch**: 인프라는 배치 로직을 모름
- **Global-utils → Batch**: 유틸리티는 Batch에 의존하면 안 됨

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/c9dde2b2-b2aa-4582-8c8c-d39866135def" alt="batch 모듈 사진">

- Job은 여러 Service를 조합하여 Facade 패턴으로 사용한다.
- Batch의 Service(구현체)는 대용량 데이터 처리 최적화, 청크 단위 처리 로직을 처리하며, 필요한 경우 Domain Service를 호출한다. **(Service 구현은 모듈별로 책임과 역할에 맞게 각각 구현되어야 하고 서로 협력하여야 한다)**
- Domain Service는 순수 비즈니스 로직을 담당한다.
