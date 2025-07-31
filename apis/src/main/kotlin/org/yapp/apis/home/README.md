# 🏠 **Home Module**

홈 화면에서 사용자의 개인화된 데이터를 조회하는 모듈입니다.

## 📁 **패키지 구조**

```
apis/src/main/kotlin/org/yapp/apis/home/
├── controller/
│   └── HomeController.kt          # 홈 화면 REST API
├── dto/
│   └── response/
│       └── UserHomeResponse.kt    # 홈 화면 응답 DTO
├── usecase/
│   └── HomeUseCase.kt            # 홈 화면 비즈니스 로직 오케스트레이션
└── README.md
```

## 🎯 **핵심 기능: 요즘 읽는 책 조회**

### 📋 **요즘 읽는 책 선별 정책 (최종 확정)**

#### **[1] 기록 남긴 책이 3권 이상인 경우**
- 도서 등록 상태와 관계없이 **최근 작성한 기록 기준으로 최신 기록순 3권** 노출
- 예시: 8/3 기록(독서완료), 8/2 기록(읽는중), 8/1 기록(읽기전)

#### **[2] 기록 남긴 책이 1~2권인 경우**
- **기록 남긴 도서 전부 포함** (동일하게 최신 기록순 노출)
- **나머지는 기록 없이 등록만 한 도서로 보충**
- 보충 우선순위: **읽는 중 → 독서 완료 → 읽기 전**

#### **[3] 기록 남긴 책이 0권인 경우**  
- **등록만 된 도서 중에서 3권까지** 보충
- 선정 우선순위: **읽는 중 → 독서 완료 → 읽기 전**

#### **📝 특별 규칙**
- **내 도서가 1-2개인 경우**: 굳이 3개 채우지 않고 해당 책만 노출
- **기록 없는 책**: 등록일(`createdAt`)을 `lastRecordedAt`으로 사용

---

### 🏗️ **구현 아키텍처**

#### **Application Service (UserBookService)**
```kotlin
selectBooksByRecordCount() {
    // 1. 기록이 있는 책들 조회
    val booksWithRecords = findBooksWithRecordsOrderByLatest()
    
    // 2. 개수에 따른 분기 처리
    when (booksWithRecords.size) {
        >= 3 -> 최신 기록순 3권 반환
        1~2 -> 기록 있는 책 + 기록 없는 책으로 보충
        0 -> 기록 없는 책만으로 상태 우선순위로 구성
    }
}
```

#### **Domain Service (UserBookDomainService)**
```kotlin
// 기록이 있는 책만 조회 (INNER JOIN)
findBooksWithRecordsOrderByLatest()

// 기록이 없는 책을 상태 우선순위로 조회 (NOT EXISTS)
findBooksWithoutRecordsByStatusPriority()
```

#### **Repository Layer**
```kotlin
// INNER JOIN으로 기록이 있는 책만
findUserBooksWithRecords(userId): Pair<UserBook, LocalDateTime>

// NOT EXISTS로 기록이 없는 특정 상태 책들
findUserBooksWithoutRecordsByStatus(userId, status, limit, excludeIds): List<UserBook>
```

---

### 🎯 **쿼리 최적화 포인트**

#### **1. 기록 유무 분리**
- **기록 있는 책**: `INNER JOIN readingRecord` (정확한 조건)
- **기록 없는 책**: `NOT EXISTS readingRecord` (정확한 조건)

#### **2. 상태별 우선순위 조회**
- 1순위: `status = 'READING'` + `NOT EXISTS readingRecord`
- 2순위: `status = 'COMPLETED'` + `NOT EXISTS readingRecord` 
- 3순위: `status = 'BEFORE_READING'` + `NOT EXISTS readingRecord`

#### **3. 중복 제거**
- 각 단계에서 이미 선택된 `bookId`들을 `NOT IN` 조건으로 제외

---

### 📊 **시나리오별 동작 예시**

#### **시나리오 1: 기록 4권 (3권 이상)**
```
기록 있는 책: [A(8/5), B(8/3), C(8/1), D(7/30)]
→ 결과: [A, B, C] (최신 기록순 3권)
```

#### **시나리오 2: 기록 1권 (1~2권)**  
```
기록 있는 책: [A(8/5)]
기록 없는 책: [B(읽는중), C(완료), D(읽기전)]
→ 결과: [A, B(등록일), C(등록일)] (기록 1 + 보충 2)
```

#### **시나리오 3: 기록 0권**
```
기록 없는 책: [A(읽는중), B(읽는중), C(완료), D(읽기전)]  
→ 결과: [A(등록일), B(등록일), C(등록일)] (상태 우선순위)
``` 