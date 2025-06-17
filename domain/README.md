# Domain Modules

## 🔑 Key Role

- **순수 비즈니스 로직**: 핵심 도메인 규칙과 비즈니스 로직만 담당하는 순수한 계층
- **도메인 엔티티 관리**: User, Book 등 핵심 비즈니스 객체의 상태와 행동 정의
- **비즈니스 규칙 구현**: 주문 생성, 결제 처리, 재고 관리 등 핵심 도메인 로직
- **데이터 무결성 보장**: Entity와 Value Object를 통한 도메인 데이터 일관성 유지
- **Repository 인터페이스 정의**: 데이터 저장소에 대한 추상화된 접근 방법 제공
- **외부 계층 독립성**: APIs, Admin, Batch, Gateway 등 상위 계층을 전혀 모르는 순수 영역

## ⛓️‍💥 Dependency Rule

```bash
Domain ─────────→ Global-utils
   │
   └─────────→ External Libraries (JPA Annotations, Database Types)
```

### ✅ Permitted Dependency Rule

- **Domain → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 검증, 암호화 등)
- **Domain → External Libraries**: JPA/Hibernate Annotations, Database Types, Validation Annotations

### ❌ Forbidden Dependency Rule

- **Domain → Gateway**: 도메인은 보안 계층을 모름 (JWT, 인증/인가, 필터 등)
- **Domain → Admin**: 도메인은 관리자 기능을 모름 (관리자 권한, 시스템 설정 등)
- **Domain → APIs**: 도메인은 API 계층을 모름
- **Domain → Batch**: 도메인은 배치 처리를 모름 (스케줄링, 대용량 처리 등)
- **Domain → Infra: 도메인은 구현 기술을 모름 (JPA 구현체, Database 연결 등)**
- **Gateway → Domain**: 보안 계층이 비즈니스 로직을 알면 안 됨
- **Global-utils → Domain**: 유틸리티는 도메인에 의존하지 않음 (순수 유틸리티 유지)

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/76b1ab11-518b-4900-b14d-7f5f3e688dfb" alt="domain 모듈 사진">

- Entity와 Value Object는 도메인의 핵심 비즈니스 개념을 표현한다. Entity는 식별 가능한 비즈니스 객체를 나타내고, Value Object는 변경 불가능한 속성들을 가진 객체를 모델링한다.
- Domain Service(구현체)는 순수 비즈니스 로직을 처리한다. (**Service 구현은 모듈별로 책임과 역할에 맞게 각각 구현되어야 하고 서로 협력하여야 한다)**
- Repository Interface는 데이터 저장소에 대한 추상화된 접근 방식을 정의하고, 실제 데이터 처리 로직은 Infrastructure 계층에서 구현하여 도메인과 기술 인프라 모듈을 분리한다.
- **해당 모듈은 InfraBaseConfigImportSelector를 통한 필요한 Infrastructure 설정 선택을 진행하지 않고, Domain만의 Config를 별도로 관리한다. (Infra ↔ domain 상호 의존 방지)**
