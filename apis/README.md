# APIs Modules

## 🔑 Key Role

- **일반 사용자 비즈니스 로직**: USER 권한 사용자를 위한 핵심 비즈니스 기능 처리
- **인증 후 서비스 제공**: Gateway에서 JWT 검증 완료 후 실제 비즈니스 로직 실행
- **JWT 토큰 생성**: Gateway의 JwtTokenProvider를 DI하여 로그인 성공 시 토큰 생성
- **사용자 대상 API**: 회원가입, 로그인, 프로필 관리, 상품 구매, 게시글 관리 등
- **파일 처리**: 이미지, 문서 업로드/다운로드 및 관련 비즈니스 로직
- **실시간 기능**: 알림, 메시징 등 즉시 응답이 필요한 사용자 서비스
- **SecurityContext 활용**: Gateway에서 설정한 인증 정보를 바탕으로 비즈니스 로직 처리

## ⛓️‍💥 Dependency Rule

```bash
APIs ────────→ Gateway ─────→ Global-utils
  │               │
  │               └─────→ External Libraries (JWT, Redis, Spring Security)
  │
  ├─────→ Domain ─────────→ Global-utils
  │          │
  │          └─────────→ External Libraries (JPA, Database)
  │
  ├─────→ Infra ──────────→ Domain
  │          │
  │          ├─────────→ Global-utils
  │          │
  │          └─────────→ External Libraries (Spring Data JPA, Redis, Kafka, AWS SDK)
  │
  ├─────→ Global-utils
  │
  └─────→ External Libraries (Spring Web, Validation, File Upload, etc.)
```

### ✅ Permitted Dependency Rule

- **APIs → Gateway**: **JwtTokenProvider만 사용** (로그인 시 토큰 생성 목적)
- **APIs → Domain**: 공통 비즈니스 로직 호출 (UserDomainService, ProductDomainService 등)
- **APIs → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 검증, 암호화 등)
- **APIs → Infra**: InfraBaseConfigImportSelector를 통한 필요한 Infrastructure 설정 선택
- **APIs → External Libraries**: Spring Web, Validation, File Upload 등

### ❌ Forbidden Dependency Rule

- **APIs → Gateway (Filter/Config): Gateway의 보안 필터나 설정에는 의존하지 않음**
- **APIs → Admin**: 같은 계층끼리는 서로 의존하지 않음
- **APIs → Batch**: API는 배치 작업을 직접 호출하지 않음
- **Gateway → APIs**: Gateway는 API 비즈니스 로직을 알면 안 됨
- **Admin → APIs**: 관리자 모듈이 사용자 API를 직접 호출하지 않음
- **Batch → APIs**: 배치는 HTTP 요청 처리와 무관
- **Domain → APIs**: 도메인은 API를 모르는 순수 영역
- **Infra → APIs**: 인프라는 상위 계층을 모름
- **Global-utils → APIs**: 공통 유틸리티는 API에 의존하지 않음

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/65cf1a06-eeb7-4c22-a73b-eb8ecc8cca9a" alt="api 모듈 사진">

- UseCase는 여러 Apis Service를 조합하여 Facade 패턴으로 사용한다.
- Admin의 Service(구현체)는 관리자 전용 비즈니스 로직을 처리하며, 필요한 경우 Domain Service를 호출한다. (**Service 구현은 모듈별로 책임과 역할에 맞게 각각 구현되어야 하고 서로 협력하여야 한다)**
- Domain Service는 순수 비즈니스 로직을 담당한다.
