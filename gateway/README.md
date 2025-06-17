# Gateway Modules

## 🔑 Key Role

- **인증/인가 관문**: 모든 HTTP 요청의 JWT 토큰 검증 및 권한 체크
- **보안 필터링**: 악성 요청 차단, XSS/SQL Injection 방지
- **횡단 관심사 처리**: 보안 로깅, Rate Limiting, CORS 설정
- **JWT 유틸리티 제공**: 토큰 생성/검증 기능을 다른 모듈에서 사용 가능
- **단일 진입점**: 모든 요청이 거쳐가는 보안 체크포인트 역할

## ⛓️‍💥 Dependency Rule

```bash
Gateway ─→ Global-utils
  │ 
  └─→ External Libraries (JWT, Redis, Spring Security)
```

### ✅ Permitted Dependency Rule

- **APIs → Gateway**: **JwtTokenProvider만 사용 (로그인 시 토큰 생성 목적)**
- **Gateway → Global-utils**: 공통 유틸리티 사용 (날짜, 문자열, 암호화 등)
- **Gateway → External Libraries**: JWT, Redis, Spring Security 등

### ❌ Forbidden Dependency Rule

- **APIs → Gateway (Filter/Config): Gateway의 보안 필터나 설정에는 의존하지 않음**
- **Admin → Gateway**: Admin은 Gateway의 JWT 기능을 사용하지 않음
- **Batch → Gateway**: Batch는 HTTP 보안과 무관
- **Domain → Gateway**: 도메인은 Gateway를 모르는 순수 영역
- **Infra → Gateway**: 인프라는 보안 계층을 알면 안 됨
- **Global-utils → Gateway**: 공통 유틸리티는 어느 것도 의존하지 않음
- **Gateway → Admin**: Gateway는 관리 로직을 알면 안 됨
- **Gateway → APIs**: Gateway는 비즈니스 로직 모듈을 알면 안 됨
- **Gateway → Batch**: Gateway는 배치 스케줄링을 알면 안 됨
- **Gateway → Domain**: Gateway는 도메인 로직에 의존하지 않음
- **Gateway → Infra**: Gateway는 인프라 구현체를 직접 의존하지 않음

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/42efae71-f409-41de-b21d-28401b7fbb45" alt="gateway 모듈 사진">

- 각 필터가 chain.doFilter()로 다음 필터에게 요청 전달해서 보안 처리를 담당한다.
- JwtTokenProvider는 gateway 모듈에 정의되어 있으며, apis 모듈에서는 Spring의 의존성 주입(DI)을 통해 해당 Bean을 주입받아 사용한다.
