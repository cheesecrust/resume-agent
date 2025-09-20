# 자소서 AI 애플리케이션 설정 가이드

## 개요

이 가이드는 프론트엔드(Next.js)와 백엔드(Spring Boot)를 연결하여 AI 자소서 생성 애플리케이션을 실행하는 방법을 설명합니다.

## 필수 요구사항

- **Java 17** 이상
- **Node.js 18** 이상
- **OpenAI API 키**

## 설정 순서

### 1. 백엔드 설정

1. **환경변수 설정**
   ```bash
   cd backend
   cp .env.example .env

   # .env 파일에서 다음 값 설정:
   # OPENAI_API_KEY=your-actual-openai-api-key
   ```

2. **백엔드 실행**
   ```bash
   # Gradle로 실행
   ./gradlew bootRun

   # 또는 IntelliJ에서 ResumeAiApplication.java 실행
   ```

3. **백엔드 확인**
   ```bash
   curl http://localhost:8080/api/health
   # "Resume AI Backend is running!" 응답 확인
   ```

### 2. 프론트엔드 설정

1. **환경변수 설정**
   ```bash
   cd frontend
   cp .env.example .env.local

   # .env.local 파일 확인 (이미 올바른 값으로 설정됨):
   # NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
   ```

2. **의존성 설치**
   ```bash
   npm install
   ```

3. **프론트엔드 실행**
   ```bash
   npm run dev
   ```

4. **프론트엔드 확인**
   - 브라우저에서 `http://localhost:3000` 접속
   - "자소서 AI" 홈페이지 확인

### 3. 연결 테스트

1. **전체 플로우 테스트**
   - `http://localhost:3000` 접속
   - "시작하기" 버튼 클릭
   - 자소서 정보 입력:
     - AI 모델: GPT-4 선택
     - 자소서 문항: "지원동기를 작성해주세요"
     - 초안: "저는 이 회사에 지원합니다"
     - 글자수 제한: 500
     - 회사명: "테스트 회사"
     - 직군: "개발자"
   - "자소서 생성하기" 버튼 클릭
   - AI 생성 결과 확인

## 포트 정보

- **프론트엔드**: http://localhost:3000
- **백엔드**: http://localhost:8080

## 환경변수 설명

### 백엔드 (.env)
- `OPENAI_API_KEY`: OpenAI API 키 (필수)
- `CLAUDE_API_KEY`: Claude API 키 (선택, 향후 지원)
- `GOOGLE_API_KEY`: Google Gemini API 키 (선택, 향후 지원)

### 프론트엔드 (.env.local)
- `NEXT_PUBLIC_BACKEND_URL`: 백엔드 서버 URL
- `NEXT_PUBLIC_APP_NAME`: 애플리케이션 이름
- `NEXT_PUBLIC_APP_DESCRIPTION`: 애플리케이션 설명

## 문제 해결

### 1. 백엔드 실행 오류

**"OPENAI_API_KEY not found" 오류**
```bash
# 환경변수 확인
echo $OPENAI_API_KEY

# 환경변수 설정 (임시)
export OPENAI_API_KEY=your-api-key

# 또는 .env 파일 확인
cat backend/.env
```

**포트 8080 이미 사용 중**
```bash
# 포트 사용 프로세스 확인
netstat -tulpn | grep 8080

# application.yml에서 포트 변경
# server:
#   port: 8081
```

### 2. 프론트엔드 연결 오류

**백엔드 연결 실패**
```bash
# 백엔드 실행 상태 확인
curl http://localhost:8080/api/health

# 환경변수 확인
cat frontend/.env.local
```

**CORS 오류**
- 백엔드의 `application.yml`에서 CORS 설정 확인
- 프론트엔드 URL이 `cors.allowed-origins`에 포함되어 있는지 확인

### 3. API 호출 오류

**OpenAI API 호출 실패**
- API 키가 유효한지 확인
- OpenAI 계정에 충분한 크레딧이 있는지 확인
- 네트워크 연결 상태 확인

## 개발 팁

### 1. 로그 확인

**백엔드 로그**
```bash
# 실시간 로그 확인
./gradlew bootRun --debug
```

**프론트엔드 로그**
```bash
# 브라우저 개발자 도구 > 콘솔 탭 확인
# 또는 터미널에서 Next.js 로그 확인
```

### 2. 개발 모드

**백엔드 자동 재시작**
```bash
./gradlew bootRun -Pdev
```

**프론트엔드 Hot Reload**
```bash
npm run dev
# 파일 변경 시 자동으로 새로고침
```

### 3. 빌드 및 배포

**프로덕션 빌드**
```bash
# 백엔드
./gradlew bootJar

# 프론트엔드
npm run build
```

## 추가 설정

### 데이터베이스 연결 (향후 확장)
- PostgreSQL 또는 MySQL 설정
- JPA 엔티티 구성
- 사용자 정보 및 자소서 히스토리 저장

### 보안 강화
- JWT 기반 인증 구현
- API Rate Limiting
- 입력 검증 강화

### 모니터링
- Spring Boot Actuator 활용
- 로그 수집 및 분석
- 성능 모니터링