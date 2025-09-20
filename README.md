# 🚀 Resume AI - 자소서 AI 생성 서비스

AI에게 그냥 요청을 하게 되면 작성한 글의 글자 수를 정확히 카운팅하지 못하고, 글자수 제한을 두면 휠씬 못 미치게 작성하는 경우가 있습니다. 따라서 분량을 90% 정도는 채우도록 개선합니다.

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)
![Next.js](https://img.shields.io/badge/Next.js-14-black.svg)
![Spring AI](https://img.shields.io/badge/Spring%20AI-0.8.1-green.svg)

## 📋 목차

- [🌟 주요 기능](#-주요-기능)
- [🛠 기술 스택](#-기술-스택)
- [📁 프로젝트 구조](#-프로젝트-구조)
- [⚡ 빠른 시작](#-빠른-시작)
- [🔧 설치 및 실행](#-설치-및-실행)
- [🌍 환경 설정](#-환경-설정)
- [📚 API 문서](#-api-문서)
- [🎯 사용법](#-사용법)
- [🤝 기여하기](#-기여하기)
- [📄 라이선스](#-라이선스)

## 🌟 주요 기능

### ✨ **AI 기반 자소서 개선**
- **GPT-4** 및 **GPT-3.5 Turbo** 모델 지원
- 회사와 직군에 맞춤형 키워드 추가
- 글자수 제한에 맞는 최적화
- STAR 기법 활용한 구조화

## 🛠 기술 스택

### 🖥 **Frontend**
- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS v4
- **UI Library**: shadcn/ui (Radix UI)
- **Icons**: Lucide React
- **Font**: Geist Sans/Mono

### ⚙ **Backend**
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.5
- **AI Integration**: Spring AI 0.8.1
- **Security**: Spring Security
- **API**: RESTful API

### 🤖 **AI & External Services**
- **OpenAI**: GPT-4, GPT-3.5 Turbo
- **Future Support**: Claude 3, Gemini Pro

## 📁 프로젝트 구조

```
resume-agent/
├── 📂 frontend/                 # Next.js 프론트엔드
│   ├── 📂 app/                  # App Router 페이지
│   │   ├── 📂 api/              # API Routes
│   │   ├── 📂 write/            # 자소서 작성 페이지
│   │   ├── globals.css          # 글로벌 스타일
│   │   ├── layout.tsx           # 루트 레이아웃
│   │   └── page.tsx             # 홈페이지
│   ├── 📂 components/           # React 컴포넌트
│   │   └── 📂 ui/               # shadcn/ui 컴포넌트
│   ├── 📂 lib/                  # 유틸리티 함수
│   │   ├── config.ts            # 앱 설정
│   │   └── utils.ts             # 공통 유틸
│   └── 📂 hooks/                # 커스텀 훅
│
├── 📂 backend/                  # Spring Boot 백엔드
│   ├── 📂 src/main/java/com/resumeai/
│   │   ├── 📂 config/           # 설정 클래스
│   │   ├── 📂 controller/       # REST 컨트롤러
│   │   ├── 📂 service/          # 비즈니스 로직
│   │   ├── 📂 dto/              # 데이터 전송 객체
│   │   └── 📂 exception/        # 예외 처리
│   └── 📂 src/main/resources/
│       └── application.yml      # 애플리케이션 설정
│
├── 📄 API_DOCUMENTATION.md     # API 명세서
├── 📄 SETUP.md                 # 상세 설정 가이드
├── 📄 CLAUDE.md                # Claude Code 가이드
└── 📄 README.md                # 프로젝트 소개 (이 파일)
```

## ⚡ 빠른 시작

### 📋 **필수 요구사항**
- **Java 17** 이상
- **Node.js 18** 이상
- **OpenAI API 키**

### 🚀 **1분 만에 실행하기**

```bash
# 1. 저장소 클론
git clone https://github.com/cheesecrust/resume-agent.git
cd resume-agent

# 2. OpenAI API 키 설정
export OPENAI_API_KEY=your-openai-api-key

# 3. 백엔드 실행
cd backend
./gradlew bootRun

# 4. 새 터미널에서 프론트엔드 실행
cd frontend
npm install
npm run dev
```

**🌐 접속**: `http://localhost:3000`

## 🔧 설치 및 실행

### 🖥 **백엔드 설정**

```bash
cd backend

# 환경변수 설정
export OPENAI_API_KEY=your-openai-api-key

# Gradle 빌드 및 실행
./gradlew clean build
./gradlew bootRun

# 또는 IntelliJ에서 ResumeAiApplication.java 실행
```

**✅ 백엔드 확인**: `http://localhost:8080/api/health`

### 🎨 **프론트엔드 설정**

```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

# 프로덕션 빌드
npm run build
npm start
```

**✅ 프론트엔드 확인**: `http://localhost:3000`

## 🌍 환경 설정

### 🔑 **환경변수**

#### 백엔드 환경변수
```bash
# 필수
OPENAI_API_KEY=sk-proj-your-openai-api-key

# 선택사항
ADMIN_PASSWORD=your-admin-password
CLAUDE_API_KEY=your-claude-api-key    # 향후 지원
GOOGLE_API_KEY=your-google-api-key    # 향후 지원
```

#### 프론트엔드 환경변수
```bash
# frontend/.env.local
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME=자소서 AI
NEXT_PUBLIC_APP_DESCRIPTION=AI가 도와주는 완벽한 자기소개서 작성 서비스
```

### ⚙ **포트 설정**
- **프론트엔드**: `3000` (Next.js)
- **백엔드**: `8080` (Spring Boot)

## 📚 API 문서

### 🔗 **주요 엔드포인트**

| 메서드 | 엔드포인트 | 설명 |
|--------|------------|------|
| `POST` | `/api/generate-resume` | 자소서 생성/개선 |
| `GET` | `/api/health` | 헬스 체크 |
| `GET` | `/actuator/health` | Spring Actuator 헬스 |

### 📖 **상세 API 문서**
- 📄 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- 📄 [SETUP.md](./SETUP.md)

### 🧪 **API 테스트 예시**

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

## 🎯 사용법

### 1️⃣ **홈페이지 접속**
- `http://localhost:3000` 접속
- "시작하기" 버튼 클릭

### 2️⃣ **자소서 정보 입력**
- **AI 모델**: GPT-4 (권장) 또는 GPT-3.5 Turbo
- **자소서 문항**: 지원하는 회사의 문항
- **초안**: 작성한 자소서 초안
- **글자수 제한**: 최대 글자수
- **회사명**: 지원 회사 이름
- **직군**: 지원 직무/직군

### 3️⃣ **AI 자소서 생성**
- "자소서 생성하기" 버튼 클릭
- AI가 개선된 자소서와 코멘트 제공

### 4️⃣ **결과 확인**
- 개선된 자소서 내용 확인
- AI의 개선 코멘트 참고

## 🏗 개발 환경

### 🔧 **개발 도구**
```bash
# 백엔드 개발
./gradlew bootRun -Pdev  # 자동 재시작

# 프론트엔드 개발
npm run dev              # Hot reload
```

### 🧪 **테스트**
```bash
# 백엔드 테스트
./gradlew test

# 프론트엔드 린트
npm run lint
```

## 🚨 문제 해결

### ❗ **자주 발생하는 문제**

1. **OpenAI API 키 오류**
   ```bash
   export OPENAI_API_KEY=your-actual-api-key
   ```

2. **포트 충돌**
   ```yaml
   # backend/src/main/resources/application.yml
   server:
     port: 9000  # 포트 변경
   ```

3. **CORS 오류**
   - `CorsConfig.java`에서 프론트엔드 URL 확인

### 📞 **지원**
- 🐛 [Issues](https://github.com/cheesecrust/resume-agent/issues)
- 📖 [상세 설정 가이드](./SETUP.md)

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🛣 로드맵

### ✅ **완료된 기능**
- [x] OpenAI GPT-4/3.5 통합
- [x] 반응형 웹 UI
- [x] Spring Security 보안
- [x] 타입스크립트 지원

### 🔮 **향후 계획**
- [ ] 사용자 계정 및 로그인
- [ ] 자소서 히스토리 저장
- [ ] Claude 3 모델 지원
- [ ] Google Gemini 연동
- [ ] PDF 내보내기
- [ ] 템플릿 시스템
- [ ] 다국어 지원

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 👨‍💻 개발자

**cheesecrust** - [GitHub](https://github.com/cheesecrust)

---

### 🙏 **특별 감사**

- [Spring AI](https://spring.io/projects/spring-ai) - AI 통합 프레임워크
- [Next.js](https://nextjs.org/) - React 프레임워크
- [shadcn/ui](https://ui.shadcn.com/) - UI 컴포넌트 라이브러리
- [Tailwind CSS](https://tailwindcss.com/) - CSS 프레임워크

---

**🚀 Generated with [Claude Code](https://claude.ai/code)**

**📧 문의사항이나 제안사항이 있으시면 언제든 연락주세요!**