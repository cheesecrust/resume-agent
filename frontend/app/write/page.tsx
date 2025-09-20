"use client"

import type React from "react"
import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Sparkles, FileText, Target, Building2, Users, Loader2, ArrowLeft, CheckCircle, Bot } from "lucide-react"
import Link from "next/link"
import { APP_CONFIG, AI_MODELS, DEFAULT_VALUES, UI_CONFIG } from "@/lib/config"

export default function WritePage() {
  const [formData, setFormData] = useState({
    question: "",
    draft: "",
    wordLimit: DEFAULT_VALUES.wordLimit.toString(),
    company: "",
    position: "",
    aiModel: DEFAULT_VALUES.aiModel,
  })
  const [isGenerating, setIsGenerating] = useState(false)
  const [result, setResult] = useState<{
    improvedResume: string
    comments: string[]
  } | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsGenerating(true)

    try {
      const response = await fetch("/api/generate-resume", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          question: formData.question,
          draft: formData.draft,
          wordLimit: Number.parseInt(formData.wordLimit),
          company: formData.company,
          position: formData.position,
          aiModel: formData.aiModel,
        }),
      })

      if (!response.ok) {
        throw new Error("자소서 생성에 실패했습니다.")
      }

      const data = await response.json()
      setResult({
        improvedResume: data.improvedResume || "결과를 받아오지 못했습니다.",
        comments: data.comments || [],
      })
    } catch (error) {
      console.error("API 요청 실패:", error)
      setResult({
        improvedResume: "죄송합니다. 자소서 생성 중 오류가 발생했습니다. 다시 시도해주세요.",
        comments: [],
      })
    } finally {
      setIsGenerating(false)
    }
  }

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <div className="min-h-screen bg-background grid-pattern">
      {/* Header */}
      <header className="border-b border-border/50 backdrop-blur-sm bg-background/80 sticky top-0 z-50">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Link href="/">
                <Button variant="ghost" size="sm">
                  <ArrowLeft className="w-4 h-4 mr-2" />
                  홈으로
                </Button>
              </Link>
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
                  <Sparkles className="w-5 h-5 text-primary-foreground" />
                </div>
                <span className="text-xl font-bold text-foreground">{APP_CONFIG.name}</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-2">자기소개서 작성하기</h1>
          <p className="text-muted-foreground">아래 정보를 입력하면 AI가 최적화된 자기소개서를 작성해드립니다.</p>
        </div>

        <div className="grid lg:grid-cols-2 gap-8">
          {/* Input Form */}
          <Card className="glow-effect">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileText className="w-5 h-5 text-primary" />
                자기소개서 정보 입력
              </CardTitle>
              <CardDescription>모든 항목을 정확히 입력해주세요.</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="aiModel" className="flex items-center gap-2">
                    <Bot className="w-4 h-4" />
                    AI 모델 선택
                  </Label>
                  <Select value={formData.aiModel} onValueChange={(value) => handleInputChange("aiModel", value)}>
                    <SelectTrigger>
                      <SelectValue placeholder="AI 모델을 선택하세요" />
                    </SelectTrigger>
                    <SelectContent>
                      {AI_MODELS.map((model) => (
                        <SelectItem key={model.value} value={model.value}>
                          {model.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <p className="text-xs text-muted-foreground">GPT-4는 가장 정확하고 자연스러운 결과를 제공합니다.</p>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="question" className="flex items-center gap-2">
                    <Target className="w-4 h-4" />
                    자소서 문항
                  </Label>
                  <Textarea
                    id="question"
                    placeholder="예: 지원동기와 입사 후 포부를 기술해 주세요."
                    value={formData.question}
                    onChange={(e) => handleInputChange("question", e.target.value)}
                    className="min-h-[100px]"
                    maxLength={UI_CONFIG.maxQuestionLength}
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="draft">초안 작성</Label>
                  <Textarea
                    id="draft"
                    placeholder="작성하신 초안을 입력해주세요. AI가 이를 바탕으로 개선해드립니다."
                    value={formData.draft}
                    onChange={(e) => handleInputChange("draft", e.target.value)}
                    className="min-h-[150px]"
                    maxLength={UI_CONFIG.maxDraftLength}
                    required
                  />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="wordLimit">글자수 제한</Label>
                    <Input
                      id="wordLimit"
                      type="number"
                      placeholder={DEFAULT_VALUES.wordLimit.toString()}
                      min={UI_CONFIG.minWordLimit}
                      max={UI_CONFIG.maxWordLimit}
                      value={formData.wordLimit}
                      onChange={(e) => handleInputChange("wordLimit", e.target.value)}
                      required
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="company" className="flex items-center gap-2">
                      <Building2 className="w-4 h-4" />
                      회사명
                    </Label>
                    <Input
                      id="company"
                      placeholder="삼성전자"
                      value={formData.company}
                      onChange={(e) => handleInputChange("company", e.target.value)}
                      maxLength={UI_CONFIG.maxCompanyLength}
                      required
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="position" className="flex items-center gap-2">
                      <Users className="w-4 h-4" />
                      직군
                    </Label>
                    <Input
                      id="position"
                      placeholder="소프트웨어 개발"
                      value={formData.position}
                      onChange={(e) => handleInputChange("position", e.target.value)}
                      maxLength={UI_CONFIG.maxPositionLength}
                      required
                    />
                  </div>
                </div>

                <Button type="submit" className="w-full" size="lg" disabled={isGenerating}>
                  {isGenerating ? (
                    <>
                      <Loader2 className="w-5 h-5 mr-2 animate-spin" />
                      {formData.aiModel.toUpperCase()}가 자소서를 작성중입니다...
                    </>
                  ) : (
                    <>
                      <Sparkles className="w-5 h-5 mr-2" />
                      {formData.aiModel.toUpperCase()}로 자소서 개선하기
                    </>
                  )}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Result Display */}
          <Card className="glow-effect">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-primary" />
                AI 개선 결과
              </CardTitle>
              <CardDescription>AI가 분석하고 개선한 자기소개서입니다.</CardDescription>
            </CardHeader>
            <CardContent>
              {isGenerating ? (
                <div className="flex flex-col items-center justify-center py-12 space-y-4">
                  <Loader2 className="w-8 h-8 animate-spin text-primary" />
                  <p className="text-muted-foreground">AI가 최적의 자기소개서를 작성중입니다...</p>
                </div>
              ) : result ? (
                <div className="space-y-6">
                  {result.comments && result.comments.length > 0 && (
                    <div className="space-y-3">
                      <h4 className="text-sm font-semibold text-foreground flex items-center gap-2">
                        <CheckCircle className="w-4 h-4 text-green-500" />
                        AI 개선 포인트
                      </h4>
                      <div className="space-y-2">
                        {result.comments.map((comment, index) => (
                          <div key={index} className="flex items-start gap-2 text-sm">
                            <div className="w-1.5 h-1.5 rounded-full bg-primary mt-2 flex-shrink-0" />
                            <p className="text-muted-foreground leading-relaxed">{comment}</p>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  <div className="flex items-center justify-between text-sm text-muted-foreground border-b border-border pb-2">
                    <span>개선된 자기소개서</span>
                    <span className="font-medium">
                      {result.improvedResume.length}자 /{" "}
                      {formData.wordLimit ? `${formData.wordLimit}자 제한` : "제한 없음"}
                      {formData.wordLimit && result.improvedResume.length > Number.parseInt(formData.wordLimit) && (
                        <span className="text-destructive ml-2">
                          ({result.improvedResume.length - Number.parseInt(formData.wordLimit)}자 초과)
                        </span>
                      )}
                    </span>
                  </div>
                  <div className="bg-muted/50 rounded-lg p-4">
                    <pre className="whitespace-pre-wrap text-sm leading-relaxed font-sans">{result.improvedResume}</pre>
                  </div>
                  <div className="flex gap-2">
                    <Button variant="outline" size="sm">
                      복사하기
                    </Button>
                    <Button variant="outline" size="sm">
                      다시 생성
                    </Button>
                    <Button variant="outline" size="sm">
                      다운로드
                    </Button>
                  </div>
                </div>
              ) : (
                <div className="flex flex-col items-center justify-center py-12 space-y-4 text-center">
                  <FileText className="w-12 h-12 text-muted-foreground/50" />
                  <p className="text-muted-foreground">
                    왼쪽 폼을 작성하고 {formData.aiModel.toUpperCase()}로 자소서 개선하기 버튼을 클릭하세요.
                  </p>
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
