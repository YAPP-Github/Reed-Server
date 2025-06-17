# Global-utils Modules

## 🔑 Key Role

- **공통 유틸리티 제공**: 모든 계층에서 공통적으로 사용되는 범용 기능 제공
- **기술적 순수성 유지**: 비즈니스 로직이나 도메인 지식을 포함하지 않는 순수 기술적 유틸리티
- **의존성 없는 독립 모듈**: 다른 모든 모듈의 기반이 되는 최하위 계층으로 어떤 모듈에도 의존하지 않음
- **최대 재사용성**: 프로젝트 전체 계층에서 의존성 충돌 없이 안전하게 활용 가능한 범용 기능 제공
- **표준화된 공통 기능**: 날짜, 문자열, 암호화, 검증 등 표준화된 처리 방식 제공

## ⛓️‍💥 Dependency Rule

```bash
Global-utils ─────→ External Libraries Only (Apache Commons, Jackson, etc.)
    ↑
    │ (모든 모듈이 Global-utils에 의존)
    │
    ├── Gateway
    ├── Admin  
    ├── Batch
    ├── APIs
    ├── Domain
    └── Infra
```

### ✅ Permitted Dependency Rule

- **모든 모듈 → Global-utils**: 모든 상위 모듈에서 Global-utils 사용 가능
- **Global-utils → External Libraries**: Apache Commons, Jackson, Spring Core, Validation 등

### ❌ Forbidden Dependency Rule

- **Global-utils → 다른 모든 모듈**: Gateway, Admin, Batch, APIs, Domain, Infra 등 어떤 모듈에도 의존하지 않음

## **🏛️ Architecture**

<img src="https://github.com/user-attachments/assets/a05bf4ce-2dda-42e1-8765-34a9381d7c77" alt="global-utils 모듈 사진">

- 가능하면 사용하지 않으며, 해당 모듈에 기능을 추가할 때는 팀원과 상의 후 결정한다.
- Global-utils는 모든 계층의 기반이 되는 순수 유틸리티 계층으로, 어떤 비즈니스 로직이나 기술 구현체에도 의존하지 않는다.
- 모든 상위 모듈에서 안전하게 사용할 수 있도록 설계되며, 재사용성을 최우선으로 한다.
- 외부 라이브러리만을 의존하여 기술적 순수성을 유지한다.
