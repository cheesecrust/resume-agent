# API 명세서 (API Documentation)

## 개요

자소서 AI 애플리케이션에서 사용되는 백엔드 API 명세서입니다. 프론트엔드는 Next.js 14로 구축되어 있으며, 백엔드 서버와 통신하여 AI 기반 자기소개서 개선 서비스를 제공합니다.

## 기본 정보

- **Base URL**: `YOUR_BACKEND_SERVER_URL` (환경변수로 설정 필요)
- **Content-Type**: `application/json`
- **Authentication**: Bearer Token (선택사항)

## API 엔드포인트

### 1. 자기소개서 생성/개선 API

#### `POST /api/generate-resume`

AI를 사용하여 사용자의 자기소개서 초안을 개선하고 최적화하는 API입니다.

#### Request

**Headers**
```http
Content-Type: application/json
Authorization: Bearer {API_KEY} (선택사항)
```

**Body Parameters**
```json
{
  "question": "string",        // 자소서 문항 (필수)
  "draft": "string",          // 사용자 초안 (필수)
  "wordLimit": "number",      // 글자수 제한 (필수)
  "company": "string",        // 지원 회사명 (필수)
  "position": "string",       // 지원 직군/직무 (필수)
  "aiModel": "string",        // 사용할 AI 모델 (기본값: "gpt-4")
  "includeComments": "boolean" // 개선 코멘트 포함 여부 (기본값: true)
}
```

**Field Descriptions**

| 필드 | 타입 | 필수 | 설명 | 예시 |
|------|------|------|------|------|
| `question` | string | ✅ | 자기소개서 문항 질문 | "지원동기와 입사 후 포부를 기술해 주세요." |
| `draft` | string | ✅ | 사용자가 작성한 초안 | "저는 이 회사에 지원하게 된 이유는..." |
| `wordLimit` | number | ✅ | 글자수 제한 (최대 글자수) | 1000 |
| `company` | string | ✅ | 지원하려는 회사명 | "삼성전자" |
| `position` | string | ✅ | 지원하려는 직군/직무 | "프론트엔드 개발자" |
| `aiModel` | string | ❌ | 사용할 AI 모델 | "gpt-4" |
| `includeComments` | boolean | ❌ | 개선 코멘트 포함 여부 | true |

**지원되는 AI 모델**
- `gpt-4` (기본값, 추천)
- `gpt-3.5-turbo`
- `claude-3`
- `gemini-pro`

#### Response

**Success (200 OK)**
```json
{
  "improvedResume": "string",  // 개선된 자기소개서 내용
  "comments": [               // 개선 사항에 대한 코멘트 배열
    "string",
    "string",
    "..."
  ]
}
```

**Error (500 Internal Server Error)**
```json
{
  "error": "string",          // 에러 메시지
  "improvedResume": "string", // 기본 에러 메시지
  "comments": []              // 빈 배열
}
```

**Response Field Descriptions**

| 필드 | 타입 | 설명 |
|------|------|------|
| `improvedResume` | string | AI가 개선한 자기소개서 내용 |
| `comments` | string[] | 개선 사항에 대한 설명 코멘트들 |
| `error` | string | (에러 시) 에러 메시지 |

#### 예시

**Request Example**
```bash
curl -X POST "YOUR_BACKEND_SERVER_URL/api/generate-resume" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_API_KEY" \
  -d '{
    "question": "지원동기와 입사 후 포부를 기술해 주세요.",
    "draft": "저는 이 회사에 지원하게 된 이유는 성장하고 싶어서입니다.",
    "wordLimit": 1000,
    "company": "삼성전자",
    "position": "프론트엔드 개발자",
    "aiModel": "gpt-4",
    "includeComments": true
  }'
```

**Response Example**
```json
{
  "improvedResume": "저는 삼성전자의 프론트엔드 개발자로 지원하게 된 이유는 첫째, 글로벌 기술 리더로서 혁신적인 사용자 경험을 창조하는 삼성전자의 비전에 깊이 공감하기 때문입니다. 둘째, 다양한 디바이스와 플랫폼에서 일관된 사용자 인터페이스를 구현하는 기술적 도전에 매력을 느꼈습니다...",
  "comments": [
    "문장 구조를 더 명확하게 개선했습니다.",
    "회사와 직군에 맞는 키워드를 추가했습니다.",
    "글자수 제한에 맞게 내용을 조정했습니다.",
    "GPT-4 모델을 사용하여 최적화했습니다."
  ]
}
```

## 에러 처리

### 클라이언트 에러 처리

프론트엔드에서는 다음과 같이 에러를 처리합니다:

1. **네트워크 에러**: API 요청 실패 시 기본 에러 메시지 표시
2. **서버 에러**: 백엔드에서 500 에러 반환 시 에러 메시지와 함께 기본 안내 문구 표시
3. **타임아웃**: 요청 시간 초과 시 재시도 안내

### 백엔드 구현 시 고려사항

1. **인증**: API 키 기반 인증 구현 권장
2. **Rate Limiting**: 사용자별 요청 제한 구현
3. **입력 검증**: 모든 필수 필드 검증 및 적절한 타입 체크
4. **에러 로깅**: 상세한 에러 로그 기록
5. **타임아웃**: AI 모델 호출 시 적절한 타임아웃 설정

## 환경 설정

### 프론트엔드 환경변수
```env
# .env.local
NEXT_PUBLIC_BACKEND_URL=YOUR_BACKEND_SERVER_URL
API_KEY=YOUR_API_KEY (선택사항)
```

### 백엔드 요구사항
- 각 AI 모델별 API 키 설정
- CORS 설정 (프론트엔드 도메인 허용)
- SSL/TLS 인증서 (HTTPS 통신)

## 추가 개발 고려사항

### 향후 확장 가능한 API
1. **사용자 관리 API**: 회원가입, 로그인, 프로필 관리
2. **자소서 템플릿 API**: 직군별 템플릿 제공
3. **자소서 히스토리 API**: 작성 기록 저장 및 조회
4. **피드백 API**: 사용자 만족도 및 개선 요청

### 보안 고려사항
1. **개인정보 보호**: 자소서 내용 암호화 저장
2. **API 보안**: JWT 토큰 기반 인증
3. **입력 검증**: SQL Injection, XSS 방지
4. **데이터 보존**: 개인정보 보존 정책에 따른 데이터 삭제