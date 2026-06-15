# 🍽️ 모해먹지 (Mohe Meokji)

<div align="center">
  <h1>모해먹지 - 냉장고 속 재료로 식사를 해결하는 AI 가이드</h1>
  <p>🤖 AI 기반 맞춤형 레시피 추천 및 냉장고 관리 서비스 🤖</p>
</div>

<br/>

<div align="center">
  <a href="#">홈페이지</a>
    |  
  <a href="http://localhost:8080/swagger-ui/index.html">Swagger</a>
    |  
  <a href="#">Figma</a>
</div>

---

## ✍️ 프로젝트 개요

- **프로젝트명:** 모해먹지 (Mohe Meokji)
- **프로젝트 기간:** 2025-09-01 ~ 2026-04-30
- **프로젝트 형태:** WEB 서비스
- **서비스 상태:** 개발 중
- **목표:** Google Gemini AI를 활용하여 냉장고 재료 기반 레시피 추천 및 스마트 냉장고 관리 서비스 구축
- **주요 타겟 사용자:**
  - 냉장고 속 재료로 만들 수 있는 요리를 찾는 사용자
  - 식재료의 유통기한을 효과적으로 관리하고 싶은 사용자
  - AI 기반 스마트한 장보기 리스트를 원하는 사용자

---

## ✍️ 프로젝트 소개

### 프로젝트 배경

현대인들은 냉장고에 있는 재료가 무엇인지 기억하지 못하거나, 어떤 요리를 만들 수 있는지 알지 못해 음식물 낭비가 발생합니다. 또한 유통기한이 임박한 재료를 관리하기가 어렵고, 요리할 때 부족한 재료를 일일이 확인해야 합니다.

모해먹지는 이러한 문제를 해결하기 위해 냉장고 재료를 체계적으로 관리하고, Google Gemini AI를 활용하여 보유한 재료로 만들 수 있는 레시피를 추천합니다. 또한 이미지 인식을 통해 식재료를 자동으로 감지하고, 레시피 기반 스마트 장보기 기능을 제공합니다.

### 사용자 니즈

🍳 **냉장고 관리**

- 냉장고 속 재료를 한곳에서 관리하고 싶음
- 유통기한 임박 재료를 빠르게 파악하고 싶음
- 사진만 찍어도 자동으로 재료를 추가하고 싶음

🤖 **AI 레시피 추천**

- 보유한 재료로 만들 수 있는 요리를 빠르게 알고 싶음
- 마음에 드는 레시피를 저장하고 싶음
- 싫어하는 음식을 제외하고 추천받고 싶음

🛒 **스마트 장보기**

- 장보기 목록을 효율적으로 관리하고 싶음
- 레시피에 필요한 부족 재료를 자동으로 추가하고 싶음

---

## 🚀 프로젝트 목표

1. **AI를 활용한 간편한 냉장고 재료 관리 및 맞춤형 레시피 추천**

2. **이미지 인식을 통한 자동 식재료 감지 및 등록**

3. **유통기한 기반 스마트 냉장고 관리 시스템 구축**

4. **레시피 기반 효율적인 장보기 시스템 제공**

---

## ✨ 주요 기능

### 1. AI 기반 레시피 추천

- 냉장고 내 재료를 기반으로 Google Gemini AI가 요리 가능한 레시피 추천
- 권장 식재료, 조리 방법, 소요 시간 등 상세 정보 제공
- 추천 레시피 저장 및 싫어요 기능으로 개인화된 추천

<br/>

### 2. 이미지 기반 식재료 인식

- 냉장고 재료 사진을 업로드하면 Google Gemini Vision으로 자동 감지
- 감지된 재료의 이름, 카테고리, 예상 수량 자동 추출
- 한 번의 클릭으로 냉장고에 재료 등록

<br/>

### 3. 스마트 냉장고 관리

- 보유한 모든 재료를 카테고리별로 조회
- 유통기한 임박순으로 정렬된 재료 목록
- 재료 수량 수정 및 삭제 기능
- 만료된 재료 일괄 삭제

<br/>

### 4. 레시피 저장 및 관리

- 마음에 드는 레시피 저장 및 조회
- 레시피 싫어요 설정으로 개인화된 추천
- 요리 완료 처리 시 재료 자동 차감

<br/>

### 5. 스마트 장보기 목록

- 레시피 기반 부족 재료 자동 추가
- 개인 장보기 목록 관리 및 삭제
- 효율적인 장본 계획 수립

<br/>

### 6. 회원 관리

- 회원가입 및 로그인
- 현재 유저 정보 조회

---

## 🧑‍💻 팀원 소개

| **이름** | **역할** | **담당 업무** |
|:--------:|:--------:|:-------------|
| <a href="https://github.com/jiin-jung"><img src="https://github.com/jiin-jung.png" width="70px"/><br/><sub><b>나현지</b></sub></a> | FE | 프론트엔드 구현, UI/UX 설계 및 API 연동 |

---

## ⚙️ 기술 스택

<table>
  <thead>
    <tr>
      <th>분류</th>
      <th>기술 스택</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Backend</td>
      <td>
        <img src="https://img.shields.io/badge/Java_21-007396?style=flat&logo=openjdk&logoColor=white"/>
        <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white"/>
        <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>Database</td>
      <td>
        <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>AI & External API</td>
      <td>
        <img src="https://img.shields.io/badge/Google_Gemini-4285F4?style=flat&logo=google&logoColor=white"/>
        <img src="https://img.shields.io/badge/YouTube_API-FF0000?style=flat&logo=youtube&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>Build & Testing</td>
      <td>
        <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white"/>
        <img src="https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=junit5&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>Documentation</td>
      <td>
        <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black"/>
      </td>
    </tr>
  </tbody>
</table>

---

## 🏗️ 프로젝트 구조

```
com.mohemeokji.mohemeokji/
├── auth/
│   ├── CurrentUserProvider.java        # 현재 유저 추상화
│   └── DevCurrentUserProvider.java     # 개발용 유저 공급자
│
├── config/
│   └── WebClientConfig.java            # WebClient 설정
│
├── domain/
│   ├── imageanalyze/                   # 이미지 분석 도메인
│   │   ├── controller/
│   │   │   ├── ImageAnalyzeController.java
│   │   │   └── ImageAnalyzeControllerDocs.java
│   │   ├── dto/
│   │   │   ├── DetectedIngredientDto.java
│   │   │   └── ImageAnalyzeResDto.java
│   │   ├── exception/
│   │   │   ├── ImageAnalyzeErrorCode.java
│   │   │   └── ImageAnalyzeException.java
│   │   └── service/
│   │       └── ImageAnalyzeService.java
│   │
│   ├── model/                          # 도메인 엔티티
│   │   ├── AuthProvider.java
│   │   ├── User.java
│   │   ├── Ingredient.java
│   │   ├── SavedRecipe.java
│   │   ├── SavedRecipeIngredient.java
│   │   ├── ShoppingItem.java
│   │   ├── DislikedRecipe.java
│   │   ├── HouseholdType.java
│   │   └── UserRole.java
│   │
│   ├── repository/
│   ├── service/
│   └── dto/
│
├── exception/
│   ├── BusinessException.java
│   ├── EntityNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── ExternalServiceException.java
│   └── InvalidInputException.java
│
├── global/
│   ├── exception/
│   │   ├── BaseErrorCode.java
│   │   ├── GlobalErrorCode.java
│   │   └── GlobalException.java
│   └── response/
│       └── ApiResponse.java            # 공통 응답 포맷
│
├── controller/
│   ├── GlobalExceptionHandler.java
│   ├── UserController.java
│   ├── IngredientController.java
│   ├── RecipeController.java
│   └── ShoppingListController.java
│
└── DataInitializer.java
```

### 엔티티 관계도

```
User (사용자)
├─ Ingredient (냉장고 재료)
├─ SavedRecipe (저장한 레시피)
│  └─ SavedRecipeIngredient (레시피의 재료)
├─ ShoppingItem (장보기 목록)
└─ DislikedRecipe (싫어요한 레시피)
```

---

## 📡 API 문서

### 재료 관리 `/api/ingredients`

| Method | Endpoint | 설명 | Auth |
|--------|----------|------|------|
| **POST** | `/me` | 냉장고에 재료 추가 | ✅ |
| **GET** | `/me` | 내 냉장고 조회 (유통기한 임박순) | ✅ |
| **GET** | `/me/grouped` | 섹션 구분 그룹 조회 | ✅ |
| **GET** | `/shelf-life?name=` | 재료명 기준 기본 보관일 조회 | ❌ |
| **PATCH** | `/{id}/quantity` | 재료 수량 수정 | ✅ |
| **DELETE** | `/{ingredientId}` | 재료 삭제 | ✅ |
| **DELETE** | `/expired/me` | 만료 재료 일괄 삭제 | ✅ |

### 레시피 `/api/recipes`

| Method | Endpoint | 설명 | Auth |
|--------|----------|------|------|
| **GET** | `/recommendations/me` | 냉장고 재료 기반 AI 레시피 추천 | ✅ |
| **POST** | `/cook/me` | 요리 완료 처리 (재료 재고 자동 차감) | ✅ |
| **POST** | `/saved/me` | 레시피 저장 | ✅ |
| **GET** | `/saved/me` | 저장한 레시피 목록 조회 | ✅ |
| **DELETE** | `/saved/{recipeId}` | 저장 레시피 삭제 | ✅ |
| **POST** | `/dislikes/me` | 레시피 싫어요 추가 | ✅ |
| **GET** | `/dislikes/me` | 싫어요 레시피 목록 조회 | ✅ |
| **DELETE** | `/dislikes/{dislikeId}` | 싫어요 해제 | ✅ |

### 이미지 분석 `/api/analyze`

| Method | Endpoint | 설명 | Auth |
|--------|----------|------|------|
| **POST** | `/image` | 이미지에서 식재료 자동 감지 | ✅ |

**지원 형식**: `jpeg`, `png`, `webp`, `heic`  
**최대 크기**: 10MB  
**응답**: 재료명, 카테고리, 예상 수량

### 장보기 목록 `/api/shopping-list`

| Method | Endpoint | 설명 | Auth |
|--------|----------|------|------|
| **GET** | `/me` | 내 장보기 목록 조회 | ✅ |
| **POST** | `/from-recipe/me` | 레시피 기반 부족 재료 자동 추가 | ✅ |
| **DELETE** | `/{shoppingItemId}` | 항목 삭제 | ✅ |

### 유저 `/api/users`

| Method | Endpoint | 설명 | Auth |
|--------|----------|------|------|
| **POST** | `/signup` | 회원가입 | ❌ |
| **GET** | `/me` | 현재 유저 정보 조회 | ✅ |
| **GET** | `/` | 전체 유저 목록 ��회 | ✅ |

---

## 📦 공통 응답 형식

### ✅ 성공 응답

```json
{
  "isSuccess": true,
  "code": "COMMON_200",
  "message": "요청에 성공했습니다.",
  "result": {
    "id": 1,
    "name": "계란",
    "quantity": 10
  }
}
```

### ❌ 실패 응답

```json
{
  "isSuccess": false,
  "code": "IMAGE_ANALYZE_002",
  "message": "지원하지 않는 이미지 형식입니다. (jpeg, png, webp, heic만 허용)",
  "result": null
}
```

### 에러 코드

#### ImageAnalyze

| 코드 | HTTP | 설명 |
|------|------|------|
| `IMAGE_ANALYZE_001` | 400 | 이미지 파일이 비어 있음 |
| `IMAGE_ANALYZE_002` | 400 | 지원하지 않는 이미지 형식 |
| `IMAGE_ANALYZE_003` | 400 | 10MB 초과 |
| `IMAGE_ANALYZE_004` | 502 | AI 분석 중 오류 |
| `IMAGE_ANALYZE_005` | 422 | 식재료 감지 불가 |
| `IMAGE_ANALYZE_006` | 502 | AI 응답 파싱 오류 |

#### Global

| 코드 | HTTP | 설명 |
|------|------|------|
| `COMMON_200` | 200 | 성공 |
| `COMMON_400` | 400 | 잘못된 요청 |
| `COMMON_401` | 401 | 인증 실패 |
| `COMMON_404` | 404 | 리소스 미존재 |
| `COMMON_500` | 500 | 서버 오류 |

---

## 🔐 환경 설정

### 필수 환경 변수

| 변수명 | 설명 | 기본값 | 필수 |
|--------|------|--------|------|
| `DB_URL` | MySQL 접속 URL | `jdbc:mysql://localhost:3306/mohemeokji` | ✅ |
| `DB_USERNAME` | DB 사용자명 | - | ✅ |
| `DB_PASSWORD` | DB 비밀번호 | - | ✅ |
| `GEMINI_API_KEY` | Google Gemini API 키 | - | ✅ |
| `YOUTUBE_API_KEY` | YouTube Data API 키 | - | ⚠️ |
| `SERVER_PORT` | 서버 포트 | `8080` | ❌ |
| `DEV_USER_ID` | 개발용 기본 유저 ID | `2` | ❌ |

### application.yml 예시

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mohemeokji
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

server:
  port: ${SERVER_PORT:8080}

gemini:
  api-key: ${GEMINI_API_KEY}

youtube:
  api-key: ${YOUTUBE_API_KEY}
```

---

## 📚 개발 가이드

### 필수 요구사항

- **Java 21** 이상
- **MySQL 8.0** 이상 (또는 H2 for 테스트)
- **Gemini API Key** ([구하기](https://aistudio.google.com/))
- **YouTube Data API Key** ([구하기](https://developers.google.com/youtube/))

### 로컬 개발 환경 설정

```bash
# 1. 저장소 클론
git clone https://github.com/jiin-jung/mohe-meokji.git
cd mohe-meokji

# 2. MySQL 데이터베이스 생성
mysql -u root -p
CREATE DATABASE mohemeokji CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. Java 21 설치 확인
java -version

# 4. 환경 변수 설정 (.env 또는 application.yml)
export DB_URL=jdbc:mysql://localhost:3306/mohemeokji
export DB_USERNAME=root
export DB_PASSWORD=your_password
export GEMINI_API_KEY=your_gemini_key
export YOUTUBE_API_KEY=your_youtube_key

# 5. IDE에 프로젝트 임포트 (IntelliJ IDEA 추천)
# File -> Open -> mohe-meokji 폴더 선택

# 6. 의존성 설치 및 빌드
./gradlew clean build

# 7. 개발 서버 실행
./gradlew bootRun

# 8. Swagger UI 접속
# http://localhost:8080/swagger-ui/index.html
```

### 코드 스타일

- **Java**: Google Java Style Guide
- **Naming**: camelCase (variables), PascalCase (classes)
- **Comments**: 한글 코멘트 가능, 복잡한 로직은 상세히 작성

### 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스
./gradlew test --tests ImageAnalyzeServiceTest

# 테스트 커버리지
./gradlew test jacocoTestReport
```

### Git 커밋 규칙

```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 스타일 변경
refactor: 코드 리팩토링
test: 테스트 추가
chore: 빌드, 설정 변경

예: feat: Add recipe recommendation API
```

---

## 🚀 배포

### Docker 배포

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

```bash
# 빌드
./gradlew build
docker build -t mohe-meokji .

# 실행
docker run -e DB_URL=... -e GEMINI_API_KEY=... -p 8080:8080 mohe-meokji
```

---

## 📊 프로젝트 통계

- **언어**: Java
- **프레임워크**: Spring Boot 4.0.3
- **데이터베이스**: MySQL 8.0
- **라인 수**: ~5,000 LOC
- **API 엔드포인트**: 20+
- **데이터베이스 테이블**: 8개
- **외부 API 연동**: 2개 (Gemini, YouTube)

---

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

---

## 👤 개발자 정보

- **GitHub**: [@jiin-jung](https://github.com/jiin-jung)
- **Email**: cki08543@inu.ac.kr

---

<div align="center">
  <h3>🍽️ <strong>오늘도 모두 맛있게 해결하자!</strong> 🍽️</h3>
  <p>모해먹지는 AI를 통해 더 쉽고 편한 냉장고 관리 및 요리 경험을 제공합니다.</p>
</div>

---

**마지막 업데이트**: 2026-06-15
