# 🍽️ 모해먹지 (Mohe Meokji)

**냉장고 속 재료로 오늘 식사를 해결하는 AI 스마트 가이드**

---

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [기술 스택](#-기술-스택)
- [패키지 구조](#-패키지-구조)
- [주요 기능 및 API](#-주요-기능-및-api)
- [공통 응답 형식](#-공통-응답-형식)
- [실행 방법](#-실행-방법)
- [환경 변수](#-환경-변수)
- [개발자 정보](#-개발자-정보)

---

## 🎯 프로젝트 소개

냉장고에 있는 재료를 등록하면 **Google Gemini AI**가 만들 수 있는 레시피를 추천해주고, 이미지를 업로드하면 **식재료를 자동으로 인식**해주는 스마트 냉장고 관리 앱입니다.

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Build Tool | Gradle (Groovy) |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA |
| API Docs | SpringDoc (Swagger UI) |
| AI | Google Gemini API (Vision + Text) |
| External | YouTube Data API v3 |
| HTTP Client | Spring WebFlux (WebClient) |

---

## 🏗️ 패키지 구조

도메인 기준 패키징 적용.

```
com.mohemeokji.mohemeokji
├── domain/
│   └── imageanalyze/               # 이미지 식재료 분석
│       ├── controller/
│       │   ├── ImageAnalyzeController.java
│       │   └── ImageAnalyzeControllerDocs.java
│       ├── dto/
│       │   ├── DetectedIngredientDto.java
│       │   └── ImageAnalyzeResDto.java
│       ├── exception/
│       │   ├── ImageAnalyzeErrorCode.java
│       │   └── ImageAnalyzeException.java
│       └── service/
│           └── ImageAnalyzeService.java
│
├── global/
│   ├── exception/
│   │   ├── BaseErrorCode.java      # ErrorCode 인터페이스
│   │   ├── GlobalErrorCode.java    # 공통 에러 코드 Enum
│   │   └── GlobalException.java    # 베이스 예외
│   └── response/
│       └── ApiResponse.java        # 공통 응답 포맷
│
├── controller/                     # 레거시 컨트롤러 (점진적 이전 예정)
│   ├── GlobalExceptionHandler.java
│   ├── IngredientController.java
│   ├── RecipeController.java
│   ├── ShoppingListController.java
│   └── UserController.java
│
├── service/
│   ├── GeminiService.java          # Gemini 텍스트 API
│   ├── IngredientService.java
│   ├── RecipeService.java
│   ├── ShoppingListService.java
│   └── UserService.java
│
├── domain/                         # JPA 엔티티
│   ├── Ingredient.java
│   ├── SavedRecipe.java
│   ├── SavedRecipeIngredient.java  # 저장 레시피의 재료 단위
│   ├── ShoppingItem.java
│   └── User.java
│
└── auth/
    ├── CurrentUserProvider.java
    └── DevCurrentUserProvider.java
```

### 엔티티 관계

```
User ──< Ingredient            (유저의 냉장고 재료)
User ──< SavedRecipe           (유저가 저장한 레시피)
SavedRecipe ──< SavedRecipeIngredient  (레시피에 포함된 재료 목록)
User ──< ShoppingItem          (장보기 목록)
User ──< DislikedRecipe        (싫어요한 레시피)
```

---

## ✨ 주요 기능 및 API

### 재료 관리 `/api/ingredients`

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/me` | 냉장고에 재료 추가 |
| GET | `/me` | 내 냉장고 조회 (유통기한 임박순) |
| GET | `/me/grouped` | 섹션 구분 그룹 조회 |
| GET | `/shelf-life` | 재료명 기준 기본 보관일 조회 |
| PATCH | `/{id}/quantity` | 재료 수량 수정 |
| DELETE | `/{ingredientId}` | 재료 삭제 |
| DELETE | `/expired/me` | 만료 재료 일괄 삭제 |

### 레시피 `/api/recipes`

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/recommendations` | 냉장고 재료 기반 AI 레시피 추천 |
| POST | `/save` | 레시피 저장 |
| GET | `/saved` | 저장한 레시피 목록 조회 |
| DELETE | `/saved/{id}` | 저장 레시피 삭제 |

### 이미지 분석 `/api/analyze` ✨ NEW

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/image` | 이미지에서 식재료 자동 감지 |

- 지원 형식: `jpeg`, `png`, `webp`, `heic`
- 최대 크기: 10MB
- Gemini Vision API로 이미지 분석 후 재료명 / 카테고리 / 예상 수량 반환

### 장보기 목록 `/api/shopping`

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/me` | 내 장보기 목록 조회 |
| POST | `/me` | 장보기 항목 추가 |
| DELETE | `/{id}` | 항목 삭제 |

### 유저 `/api/users`

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/signup` | 회원가입 |
| GET | `/me` | 내 정보 조회 |

---

## 📦 공통 응답 형식

### 성공

```json
{
  "isSuccess": true,
  "code": "COMMON_200",
  "message": "요청에 성공했습니다.",
  "result": {}
}
```

### 실패

```json
{
  "isSuccess": false,
  "code": "IMAGE_ANALYZE_002",
  "message": "지원하지 않는 이미지 형식입니다. (jpeg, png, webp, heic만 허용)",
  "result": null
}
```

### ImageAnalyze 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| `IMAGE_ANALYZE_001` | 400 | 이미지 파일이 비어 있음 |
| `IMAGE_ANALYZE_002` | 400 | 지원하지 않는 이미지 형식 |
| `IMAGE_ANALYZE_003` | 400 | 10MB 초과 |
| `IMAGE_ANALYZE_004` | 502 | AI 분석 중 오류 |
| `IMAGE_ANALYZE_005` | 422 | 식재료 감지 불가 |
| `IMAGE_ANALYZE_006` | 502 | AI 응답 파싱 오류 |

---

## 💾 실행 방법

### 필수 요구사항

- Java 21
- MySQL 8.0
- Gemini API Key
- YouTube Data API Key

### 실행

```bash
# 저장소 클론
git clone https://github.com/jiin-jung/mohe-meokji.git
cd mohe-meokji

# 환경 변수 설정 후 실행
./gradlew bootRun
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 환경 변수

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| `DB_URL` | MySQL 접속 URL | `jdbc:mysql://localhost:3306/mohemeokji` |
| `DB_USERNAME` | DB 사용자명 | - |
| `DB_PASSWORD` | DB 비밀번호 | - |
| `GEMINI_API_KEY` | Gemini API 키 | - |
| `YOUTUBE_API_KEY` | YouTube API 키 | - |
| `SERVER_PORT` | 서버 포트 | `8080` |
| `DEV_USER_ID` | 개발용 기본 유저 ID | `2` |

---

## 👤 개발자 정보

- **GitHub**: [@jiin-jung](https://github.com/jiin-jung)
- **Email**: cki08543@inu.ac.kr

---

마지막 업데이트: 2026-04-28