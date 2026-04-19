# 🍽️ Mohe Meokji (모해먹지)
**냉장고 속 재료로 오늘 식사를 해결하는 AI 스마트 가이드**

## 📋 목차
- [프로젝트 소개](#프로젝트-소개)
- [개발 기간 및 환경](#개발-기간-및-환경)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 아키텍쳐](#프로젝트-아키텍쳐)
- [설치 방법](#설치-방법)
- [사용법](#사용법)
- [개발자 정보](#개발자-정보)
- [버그 및 피드백](#버그-및-피드백)
- [라이선스](#라이선스)
- [참고 및 출처](#참고-및-출처)
- [버전 및 업데이트 정보](#버전-및-업데이트-정보)

---

## 🎯 프로젝트 소개

**Mohe Meokji**는 냉장고 안의 재료를 입력하면 AI가 오늘 식사 메뉴를 제안해주는 스마트 가이드 애플리케이션입니다. 
식재료 낭비를 줄이고 창의적인 요리 아이디어를 얻을 수 있는 혁신적인 솔루션을 제공합니다.

### 프로젝트 목표
- 냉장고 속 재료를 효율적으로 활용
- AI 기반 맞춤형 레시피 추천
- 사용자 친화적인 UI/UX 제공
- 영양 정보 및 요리 팁 제공

---

## ⏰ 개발 기간 및 환경

| 항목 | 상세 |
|------|------|
| **개발 기간** | 2026년 3월 ~ 현재 |
| **프로젝트 생성일** | 2026년 3월 17일 |
| **주요 언어** | Java |
| **개발 환경** | Windows/macOS/Linux |
| **IDE** | IntelliJ IDEA / Eclipse |
| **버전 관리** | Git/GitHub |

---

## ✨ 주요 기능

1. **재료 입력 및 관리**
   - 냉장고 속 재료 목록 입력
   - 재료별 유통기한 관리
   - 재료 카테고리 분류

2. **AI 기반 레시피 추천**
   - 입력한 재료를 기반으로 맞춤형 요리 제안
   - 난이도별 요리 추천
   - 조리 시간 고려한 추천

3. **레시피 상세 정보**
   - 단계별 조리 방법
   - 영양 정보 표시
   - 재료별 함량 정보
   - 요리 팁 및 변형 아이디어

4. **사용자 경험 기능**
   - 즐겨찾기 저장
   - 요리 이력 관리
   - 개인 맞춤 설정

---

## 🛠️ 기술 스택

### Backend
- **Language**: Java
- **Framework**: Spring Boot (예정)
- **Database**: MySQL / PostgreSQL
- **API**: RESTful API

### Frontend
- **Language**: Java Swing / JavaFX (또는 Web: HTML/CSS/JavaScript)
- **UI Framework**: Material Design

### AI/ML
- **AI Model**: OpenAI API 또는 로컬 NLP 모델
- **데이터 처리**: Python (필요시)

### DevOps & Tools
- **Version Control**: Git/GitHub
- **Build Tool**: Maven / Gradle
- **Testing**: JUnit, Mockito
- **Documentation**: Javadoc

---

## 🏗️ 프로젝트 아키텍쳐

```
mohe-meokji/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mohe/meokji/
│   │   │       ├── controller/      # 컨트롤러
│   │   │       ├── service/         # 비즈니스 로직
│   │   │       ├── repository/      # 데이터 접근
│   │   │       ├── model/           # 데이터 모델
│   │   │       └── util/            # 유틸리티
│   │   └── resources/               # 설정 파일
│   └── test/                        # 테스트 코드
├── pom.xml                          # Maven 설정
└── README.md                        # 프로젝트 문서
```

### 주요 구성 요소

| 모듈 | 설명 |
|------|------|
| **Controller** | 사용자 요청 처리 및 응답 반환 |
| **Service** | 비즈니스 로직 구현 (AI 추천, 데이터 처리) |
| **Repository** | 데이터베이스 CRUD 작업 |
| **Model** | 재료(Ingredient), 레시피(Recipe) 등 데이터 모델 |
| **Util** | 공통 유틸리티 함수 |

---

## 💾 설치 방법

### 필수 요구사항
- Java 11 이상
- Maven 3.6+ 또는 Gradle 7.0+
- MySQL 8.0 이상
- Git

### 설치 단계

1. **저장소 클론**
```bash
git clone https://github.com/jiin-jung/mohe-meokji.git
cd mohe-meokji
```

2. **의존성 설치** (Maven 기준)
```bash
mvn clean install
```

3. **데이터베이스 설정**
```bash
# application.properties 또는 application.yml 수정
spring.datasource.url=jdbc:mysql://localhost:3306/mohe_meokji
spring.datasource.username=root
spring.datasource.password=your_password
```

4. **애플리케이션 실행**
```bash
mvn spring-boot:run
# 또는
java -jar target/mohe-meokji-1.0.0.jar
```

---

## 🚀 사용법

### 기본 사용 방법

1. **애플리케이션 시작**
   - 애플리케이션을 실행하면 메인 화면이 표시됩니다.

2. **재료 입력**
   - "재료 추가" 버튼 클릭
   - 냉장고에 있는 재료 입력 (예: 계란, 당근, 양파)
   - 유통기한 입력 (선택사항)

3. **레시피 추천 받기**
   - "추천받기" 버튼 클릭
   - AI가 입력한 재료를 기반으로 레시피 제안
   - 여러 옵션 중 선택

4. **레시피 상세 보기**
   - 추천된 레시피 선택
   - 조리 방법, 영양 정보, 팁 확인
   - 필요시 즐겨찾기 저장

### API 사용 예시 (개발자용)

```bash
# 레시피 추천 API
curl -X POST http://localhost:8080/api/recipes/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "ingredients": ["계란", "당근", "양파"],
    "cookingTime": 30,
    "difficulty": "easy"
  }'
```

---

## 👥 개발자 정보

### 프로젝트 팀

| 역할 | 담당자 | 담당 영역 |
|------|--------|----------|
| **Project Lead** | jiin-jung | 전체 프로젝트 관리, 아키텍쳐 설계 |
| **Backend Developer** | jiin-jung | Spring Boot API, 데이터베이스 설계 |
| **Frontend Developer** | jiin-jung | UI/UX 개발 |
| **AI/ML Engineer** | jiin-jung | AI 모델 통합, 추천 알고리즘 |

### 연락처
- **이메일**: [your-email@example.com]
- **GitHub**: [@jiin-jung](https://github.com/jiin-jung)

---

## 🐛 버그 및 피드백

### 버그 보고
버그를 발견하셨다면 [Issues](https://github.com/jiin-jung/mohe-meokji/issues) 페이지에서 다음 정보와 함께 보고해주세요:

- **버그 설명**: 어떤 문제가 발생했는지
- **재현 방법**: 버그를 재현하는 단계
- **예상 동작**: 정상적인 동작
- **실제 동작**: 오류 메시지 및 스크린샷
- **환경 정보**: OS, Java 버전 등

### 개선 제안
더 나은 기능이나 개선사항은 [Discussions](https://github.com/jiin-jung/mohe-meokji/discussions) 섹션에서 제안해주세요.

---

## 📄 라이선스

이 프로젝트는 **MIT License** 하에 배포됩니다.
자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

### 사용 권한
- ✅ 상업적 사용 가능
- ✅ 수정 및 배포 가능
- ✅ 개인 프로젝트에 사용 가능
- ⚠️ 저작자 표시 필수

---

## 🔗 참고 및 출처

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Java Best Practices](https://docs.oracle.com/javase/tutorial/)

### 참고 프로젝트
- 유사 프로젝트 및 레시피 데이터베이스

---

## 📊 버전 및 업데이트 정보

### 현재 버전
**v0.1.0-alpha** (개발 중)

### 버전 히스토리

| 버전 | 날짜 | 주요 변경사항 |
|------|------|---------------|
| 0.1.0-alpha | 2026-03-20 | 초기 프로젝트 구성, 기본 기능 개발 중 |

### 향후 계획 (Roadmap)

- [ ] v0.2.0: 데이터베이스 연동 완료
- [ ] v0.3.0: AI 추천 기능 완성
- [ ] v0.5.0: 웹 버전 출시
- [ ] v1.0.0: 정식 버전 출시
- [ ] v1.1.0: 모바일 앱 출시 예정

### 최근 업데이트
- **2026-03-20**: 프로젝트 초기 구성 및 GitHub 저장소 생성

---

## 📝 기여하기

이 프로젝트에 기여하고 싶으신 분들은:

1. Fork 하기
2. Feature 브랜치 생성 (`git checkout -b feature/AmazingFeature`)
3. 커밋하기 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push 하기 (`git push origin feature/AmazingFeature`)
5. Pull Request 생성하기

---

**Made with ❤️ by [jiin-jung](https://github.com/jiin-jung)**

마지막 업데이트: 2026-04-19