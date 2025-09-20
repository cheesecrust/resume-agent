// App Configuration
export const APP_CONFIG = {
  name: process.env.NEXT_PUBLIC_APP_NAME || "자소서 AI",
  description: process.env.NEXT_PUBLIC_APP_DESCRIPTION || "AI가 도와주는 완벽한 자기소개서 작성 서비스",
  version: "1.0.0",
} as const

// API Configuration
export const API_CONFIG = {
  baseUrl: process.env.NEXT_PUBLIC_BACKEND_URL || "http://localhost:8080",
  endpoints: {
    generateResume: "/api/generate-resume",
    health: "/api/health",
  },
} as const

// AI Model Configuration
export const AI_MODELS = [
  {
    value: "gpt-4",
    label: "GPT-4 (추천)",
    description: "가장 정확하고 자연스러운 결과를 제공합니다.",
  },
  {
    value: "gpt-3.5-turbo",
    label: "GPT-3.5 Turbo",
    description: "빠르고 효율적인 모델입니다.",
  },
  {
    value: "claude-3",
    label: "Claude 3",
    description: "향후 지원 예정입니다.",
  },
  {
    value: "gemini-pro",
    label: "Gemini Pro",
    description: "향후 지원 예정입니다.",
  },
] as const

// UI Configuration
export const UI_CONFIG = {
  maxWordLimit: 3000,
  minWordLimit: 100,
  defaultWordLimit: 1000,
  maxQuestionLength: 1000,
  maxDraftLength: 5000,
  maxCompanyLength: 100,
  maxPositionLength: 100,
} as const

// Default Values
export const DEFAULT_VALUES = {
  aiModel: "gpt-4",
  wordLimit: 1000,
  includeComments: true,
} as const