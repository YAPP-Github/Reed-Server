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

## **💿Database Schema Governance Strategy**
- 개발 환경에서는 ddl-auto를 활용해 스키마를 신속히 반영하고, 운영 환경에서는 Flyway 기반 마이그레이션을 통해 데이터베이스 스키마를 형상관리한다.
- Flyway 스크립트는 스키마 구조를 다루는 migration과 초기 데이터를 다루는 seed로 역할을 분리하여 관리한다.

### 1. 스키마 관리 (migration)
- 스크립트 위치는 infra 모듈의 resources/db/migration 디렉터리에 저장한다.
- 스크립트는 `V<YYYYMMDD>_<NNN>__<Action>_<Object>_<Details>.sql` 형식으로 명명한다.
- 구체적인 네이밍 규칙은 아래와 같다.
  - `V` (접두사): Versioned Migration을 의미하는 Flyway의 표준 접두사로 이 스크립트는 DB 당 단 한 번만 실행된다.
      - 예시: V
  - `<YYYYMMDD>` (버전 - 날짜): 스크립트가 작성된 날짜로, 버전 충돌을 방지하고 시간순으로 마이그레이션을 정렬하는 기준이된다.
      - 예시: 20250820 
  - _`<NNN>` (버전 - 일련번호): 같은 날짜에 여러 스크립트가 생성될 경우, 실행 순서를 보장하기 위한 3자리 일련번호로 매일 001부터 시작한다.
      - 예시: _001, _002
  - __ (구분자): 버전과 설명을 구분하는 이중 밑줄 표준 구분자이다.
  - `<Action>` (동작): 스크립트가 수행하는 주요 DDL 동작을 대문자로 명시한다.
      - 예시: CREATE, ALTER, ADD, DROP, RENAME, INSERT
  - `<Object>` (객체): 변경이 일어나는 주된 객체의 이름으로, 테이블 이름을 뜻한다.
      - 예시: USERS, BOOKS, USER_BOOKS 
  - `<Details>` (상세 설명): 변경 사항을 더 구체적으로 설명한다. 여러 단어는 밑줄(_)로 연결하고, 첫 글자만 대문자로 표기한다.
      - 예시: mail_Column, Add_Nickname, Fk_To_Users

| 상황                               | 파일 이름 예시                                                      |
|------------------------------------|---------------------------------------------------------------------|
| Users 테이블 생성                  | `V20250820_001__Create_Users.sql`                                   |
| Books 테이블에 nickname 컬럼 추가  | `V20250820_002__Add_Books_Nickname.sql`                             |
| Users 테이블의 nickname 컬럼 이름 변경 | `V20250821_001__Rename_Users_Nickname_To_Username.sql`              |
| User_books 테이블에 인덱스 추가    | `V20250821_002__Add_User_books_Index_On_UserId.sql`                 |
| Books 테이블의 title 컬럼 타입 변경 | `V20250822_001__Alter_Books_Title_To_Varchar500.sql`                 |
| 기본 Role 데이터 삽입              | `V20250822_002__Insert_Default_Roles.sql`                           |

### 2. 초기 데이터 관리 (seed)
- 스크립트 위치는 infra 모듈의 resources/db/seed 디렉터리 구조로 관리한다.
- `db/seed/common/`: 모든 환경(개발, 스테이징, 운영)에 필요한 필수 기본 데이터
- `db/seed/dev/`: 개발 환경 전용 테스트 데이터 (향후 필요시 적용)
- 스크립트는 `R__<Object>_<Data_Type>.sql` 형식으로 명명한다.
- 구체적인 네이밍 규칙은 아래와 같다.
- `R` (접두사): Repeatable Migration을 의미하는 Flyway의 표준 접두사로, 스크립트 내용이 변경될 때마다 다시 실행된다.
  - 예시: R
- __ (구분자): 접두사와 설명을 구분하는 이중 밑줄 표준 구분자이다.
- `<Object>` (객체): 데이터가 삽입되는 주된 테이블 이름을 소문자로 명시한다.
  - 예시: tags, roles, categories
- `<Data_Type>` (데이터 유형): 삽입되는 데이터의 성격을 설명한다. 여러 단어는 밑줄(_)로 연결하고, 소문자로 표기한다.
  - 예시: default_data, master_data, essential_data

| 데이터 유형                        | 파일 이름 예시                                    | 설명                                             |
|------------------------------------|---------------------------------------------------|--------------------------------------------------|
| 기본 태그 데이터                   | `R__tags_default_data.sql`                       | 시스템에서 사용하는 기본 태그들                  |
| 필수 역할 데이터                   | `R__roles_essential_data.sql`                    | USER, ADMIN 등 필수 사용자 역할                  |
| 카테고리 마스터 데이터             | `R__categories_master_data.sql`                  | 도서 분류 등 비즈니스 카테고리                   |
| 시스템 설정 데이터                 | `R__system_config_default_data.sql`              | 애플리케이션 기본 설정값들                       |

#### 데이터 삽입 가이드라인
- 고정 데이터: 절대 변경되지 않는 초기 데이터는 V__ 스크립트 사용을 우선 고려한다.
- 참조 데이터: 비즈니스 요구사항에 따라 변경될 수 있는 마스터 데이터는 R__ 스크립트를 사용한다.
- 중복 방지: INSERT IGNORE 또는 ON DUPLICATE KEY UPDATE 구문을 활용하여 중복 삽입을 방지한다.
