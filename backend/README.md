# Resume AI Backend

Spring Boot 기반의 AI 자기소개서 생성 백엔드 서비스입니다.

## 기술 스택

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Web**
- **OpenAI Java SDK**
- **Gradle 8.5**

## 주요 기능

- AI 기반 자기소개서 개선 및 생성
- 다중 AI 모델 지원 (GPT-4, GPT-3.5 Turbo)
- RESTful API 제공
- CORS 지원
- 입력 검증 및 예외 처리

## 프로젝트 구조

```
backend/
├── src/main/java/com/resumeai/
│   ├── ResumeAiApplication.java      # 메인 애플리케이션 클래스
│   ├── controller/
│   │   └── ResumeController.java     # REST API 컨트롤러
│   ├── service/
│   │   ├── AIService.java            # AI 서비스 인터페이스
│   │   ├── OpenAIService.java        # OpenAI 구현체
│   │   └── ResumeService.java        # 비즈니스 로직 서비스
│   ├── dto/
│   │   ├── AIModelType.java          # AI 모델 타입 열거형
│   │   ├── ResumeGenerationRequest.java   # 요청 DTO
│   │   └── ResumeGenerationResponse.java  # 응답 DTO
│   ├── config/
│   │   ├── CorsConfig.java           # CORS 설정
│   │   └── SecurityConfig.java       # 보안 설정
│   └── exception/
│       └── GlobalExceptionHandler.java   # 전역 예외 처리
├── src/main/resources/
│   └── application.yml               # 애플리케이션 설정
├── build.gradle                     # Gradle 빌드 설정
├── settings.gradle                  # Gradle 프로젝트 설정
├── gradle.properties                # Gradle 속성 설정
├── gradlew                         # Gradle Wrapper (Unix/Linux)
├── gradlew.bat                     # Gradle Wrapper (Windows)
└── gradle/wrapper/                 # Gradle Wrapper 파일들
```

## 환경 설정

### 1. 환경 변수 설정

다음 환경 변수를 설정하거나 `application.yml`에서 직접 값을 변경하세요:

```bash
# OpenAI API 키 (필수)
export OPENAI_API_KEY=your-openai-api-key

# 기타 선택사항
export CLAUDE_API_KEY=your-claude-api-key
export GOOGLE_API_KEY=your-google-api-key
export ADMIN_PASSWORD=your-admin-password
```

### 2. application.yml 수정

프론트엔드 도메인에 맞게 CORS 설정을 수정하세요:

```yaml
cors:
  allowed-origins:
    - "http://localhost:3000"          # 개발 환경
    - "https://your-domain.com"        # 운영 환경
```

## 실행 방법

### 개발 환경

```bash
# 1. 프로젝트 루트로 이동
cd backend

# 2. Gradle으로 의존성 설치 및 빌드
./gradlew build

# 3. 애플리케이션 실행
./gradlew bootRun

# 또는 개발 도구와 함께 실행 (자동 재시작)
./gradlew bootRun -Pdev
```

### 운영 환경

```bash
# 1. JAR 파일 빌드
./gradlew bootJar

# 2. JAR 파일 실행
java -jar build/libs/resume-ai-backend-0.0.1-SNAPSHOT.jar
```

### Windows 환경

```cmd
# Gradle Wrapper 사용 (Windows)
gradlew.bat build
gradlew.bat bootRun
```

## API 사용법

### 자기소개서 생성

```bash
curl -X POST "http://localhost:8080/api/generate-resume" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "지원동기와 입사 후 포부를 기술해 주세요.",
    "draft": "저는 이 회사에 지원하게 된 이유는...",
    "wordLimit": 1000,
    "company": "삼성전자",
    "position": "프론트엔드 개발자",
    "aiModel": "gpt-4"
  }'
```

### 헬스 체크

```bash
curl -X GET "http://localhost:8080/api/health"
```

## 지원되는 AI 모델

- **gpt-4**: OpenAI GPT-4 (권장)
- **gpt-3.5-turbo**: OpenAI GPT-3.5 Turbo
- **claude-3**: Anthropic Claude 3 (향후 지원 예정)
- **gemini-pro**: Google Gemini Pro (향후 지원 예정)

## 모니터링

Spring Boot Actuator를 통한 헬스 체크:

- `/actuator/health` - 애플리케이션 상태
- `/actuator/info` - 애플리케이션 정보
- `/actuator/metrics` - 메트릭 정보

## 로깅

- 애플리케이션 로그 레벨: DEBUG
- 요청/응답 로깅 지원
- 에러 상세 로깅

## 보안

- Spring Security 적용
- CORS 설정
- 입력 검증
- 보안 헤더 설정

## 문제 해결

### 자주 발생하는 문제

1. **OpenAI API 키 오류**
   - 환경 변수 `OPENAI_API_KEY`가 올바르게 설정되었는지 확인
   - API 키가 유효하고 충분한 크레딧이 있는지 확인

2. **CORS 오류**
   - `application.yml`의 `cors.allowed-origins`에 프론트엔드 도메인이 포함되어 있는지 확인

3. **포트 충돌**
   - 8080 포트가 이미 사용 중인 경우 `server.port` 값을 변경

## 개발자 정보

- Spring Boot 3.x 사용
- Java 17 이상 필요
- Gradle 8.5 이상 권장

## Gradle 명령어 참조

```bash
# 의존성 확인
./gradlew dependencies

# 테스트 실행
./gradlew test

# 코드 품질 검사 (추후 추가 시)
./gradlew check

# 프로젝트 정리
./gradlew clean

# 전체 빌드 (clean + build)
./gradlew clean build

# 애플리케이션 실행 (백그라운드)
./gradlew bootRun &

# 빌드 성능 분석
./gradlew build --profile
```