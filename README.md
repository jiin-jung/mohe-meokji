# 🍽️ 모해먹지 (Mohe Meokji)

**냉장고 속 재료로 오늘 식사를 해결하는 AI 스마트 가이드**

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square)
![Gradle](https://img.shields.io/badge/Gradle-Groovy-lightblue?style=flat-square)

---

## 📑 목차

- [✨ 주요 기능](#-주요-기능)
- [⚡ 빠른 시작](#-빠른-시작)
- [🛠️ 기술 스택](#-기술-스택)
- [🏗️ 프로젝트 구조](#-프로젝트-구조)
- [📡 API 문서](#-api-문서)
- [🔐 환경 설정](#-환경-설정)
- [📚 개발 가이드](#-개발-가이드)
- [🚀 배포](#-배포)
- [👤 개발자 정보](#-개발자-정보)

---

## ✨ 주요 기능

| 기능 | 설명 |
|------|------|
| 🤖 **AI 레시피 추천** | 냉장고 재료로 만들 수 있는 레시피를 Google Gemini AI가 추천 |
| 🖼️ **이미지 식재료 인식** | 사진만 업로드해도 Gemini Vision으로 자동 감지 |
| 📦 **냉장고 관리** | 유통기한 관리, 재료 재고 추적 |
| 💾 **레시피 저장** | 마음에 드는 레시피 저장 및 싫어요 설정 |
| 🛒 **스마트 장보기** | 레시피 기반 부족한 재료 자동 추가 |

---

## ⚡ 빠른 시작

### 필수 요구사항

- **Java 21** 이상
- **MySQL 8.0** 이상 (또는 H2 for 테스트)
- **Gemini API Key** ([구하기](https://aistudio.google.com/))
- **YouTube Data API Key** ([구하기](https://developers.google.com/youtube/))

### 설치 및 실행

```bash
# 저장소 클론
git clone https://github.com/jiin-jung/mohe-meokji.git
cd mohe-meokji

# 환경 변수 설정 (.env 또는 application.yml)
export DB_URL=jdbc:mysql://localhost:3306/mohemeokji
export DB_USERNAME=root
export DB_PASSWORD=your_password
export GEMINI_API_KEY=your_gemini_key
export YOUTUBE_API_KEY=your_youtube_key

# 의존성 설치 및 빌드
./gradlew build

# 서버 실행
./gradlew bootRun

# Swagger UI 접속
# http://localhost:8080/swagger-ui/index.html
```

---

## 🛠️ 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 4.0.3 |
| **Build Tool** | Gradle (Groovy) | - |
| **Database** | MySQL | 8.0 |
| **ORM** | Spring Data JPA | - |
| **API Documentation** | SpringDoc OpenAPI | 3 |
| **AI/Vision** | Google Gemini API | Vision + Text |
| **External APIs** | YouTube Data API | v3 |
| **HTTP Client** | Spring WebFlux (WebClient) | - |
| **Testing** | JUnit 5, Mockito | - |

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
| **GET** | `/` | 전체 유저 목록 조회 | ✅ |

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

### 로컬 개발 환경 설정

```bash
# 1. MySQL 설치 및 데이터베이스 생성
mysql -u root -p
CREATE DATABASE mohemeokji CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. Java 21 설치 확인
java -version

# 3. IDE에 프로젝트 임포트 (IntelliJ IDEA 추천)
# File -> Open -> mohe-meokji 폴더 선택

# 4. 의존성 다운로드
./gradlew clean build

# 5. 개발 서버 실행
./gradlew bootRun
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

**마지막 업데이트**: 2026-04-30