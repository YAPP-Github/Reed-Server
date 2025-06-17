# Infra Modules

## 🔑 Key Role

- **외부 인프라 연동**: Database, Redis, Kafka, ElasticSearch 등 외부 시스템과의 연결 관리
- **Repository 구현체 제공**: Domain에서 정의한 Repository 인터페이스의 실제 구현체 (JPA, MyBatis 등)
- **기술적 설정 관리**: Database 연결, Connection Pool, Transaction 설정 등 기술적 관심사
- **Config 클래스 제공**: 각 모듈에서 필요한 Infrastructure 설정을 선택적으로 제공
- **InfraBaseConfigImportSelector**: 각 모듈의 Config 클래스에서 필요한 Infrastructure Config를 동적으로 선택
- **외부 API 클라이언트**: 결제 서비스, 이메일 서비스, SMS 서비스 등 외부 API 연동
- **파일 시스템 연동**: 로컬 파일 시스템, AWS S3, Google Cloud Storage 등 파일 저장소 연동
- **메시징 시스템**: RabbitMQ, Apache Kafka 등 메시지 큐 시스템 연동

## ⛓️‍💥 Dependency Rule

```bash
Infra ─────────→ Domain ──────→ Global-utils
  │
  ├─────────→ Global-utils
  │
  └─────────→ External Libraries (Spring Data JPA, Redis, Kafka, AWS SDK, etc.)

APIs ─────────→ Infra
Admin ────────→ Infra
Batch ────────→ Infra
```

### ✅ Permitted Dependency Rule

- **Infra → Domain**: Repository 인터페이스 구현을 위해 Domain의 Repository Interface 의존
- **Infra → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 암호화 등)
- **Infra → External Libraries**: Spring Data JPA, Redis, Kafka, AWS SDK 등
- **APIs → Infra**: Repository 구현체, 외부 API 클라이언트, 파일 시스템 등 사용
- **Admin → Infra**: Repository 구현체, 관리자 전용 외부 시스템 연동
- **Batch → Infra**: Repository 구현체, 대용량 처리용 Database 설정

### ❌ Forbidden Dependency Rule

- **Infra → Gateway**: Infra는 보안 계층을 모름
- **Infra → Admin**: Infra는 관리 로직을 모름
- **Infra → APIs**: Infra는 상위 계층을 모름
- **Infra → Batch**: Infra는 배치 로직을 모름
- **Gateway → Infra**: Gateway는 보안 처리만 담당, Infra 의존 불필요
- **Domain → Infra: 도메인은 구현 기술을 직접 의존하지 않음 (Repository Interface만 사용)**
- **Global-utils → Infra**: 유틸리티는 Infra에 의존하지 않음

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/b39bda0e-808d-4059-b60c-50aaf517b18c" alt="infra 모듈 사진">

- Repository Implementation은 실제 데이터 저장소와의 연동을 담당하며, Repository Interface를 구현하여 JPA, MongoDB 등 구체적인 기술로 데이터를 처리한다.
- Client Implementation은 외부 시스템과의 실제 통신을 구현하며, Domain Client Interface를 구현하여 REST API, gRPC 등 구체적인 프로토콜로 통신한다.
- Import Selector는 모듈 간 설정 조합을 관리하며, admin, batch, apis 모듈 내부에서 애플리케이션 요구사항에 따라 필요한 Infrastructure 구성을 동적으로 선택한다.
