import { type NextRequest, NextResponse } from "next/server"

export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const { question, draft, wordLimit, company, position, aiModel } = body

    const backendUrl = process.env.BACKEND_API_URL || process.env.NEXT_PUBLIC_BACKEND_URL || "http://localhost:8080"

    const backendResponse = await fetch(`${backendUrl}/api/generate-resume`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // 필요한 경우 인증 헤더 추가
        // 'Authorization': `Bearer ${process.env.OPENAI_API_KEY}`,
      },
      body: JSON.stringify({
        question,
        draft,
        wordLimit,
        company,
        position,
        aiModel: aiModel || "gpt-4",
        includeComments: true,
      }),
    })

    if (!backendResponse.ok) {
      throw new Error(`Backend server error: ${backendResponse.status}`)
    }

    const result = await backendResponse.json()

    return NextResponse.json({
      improvedResume: result.improvedResume || result.result || "결과를 받아오지 못했습니다.",
      comments: result.comments || [
        "문장 구조를 더 명확하게 개선했습니다.",
        "회사와 직군에 맞는 키워드를 추가했습니다.",
        "글자수 제한에 맞게 내용을 조정했습니다.",
        `${aiModel?.toUpperCase() || "GPT-4"} 모델을 사용하여 최적화했습니다.`,
      ],
    })
  } catch (error) {
    console.error("API route error:", error)
    return NextResponse.json(
      {
        error: "자소서 생성 중 오류가 발생했습니다.",
        improvedResume: "죄송합니다. 자소서 생성 중 오류가 발생했습니다. 다시 시도해주세요.",
        comments: [],
      },
      { status: 500 },
    )
  }
}
