"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Sparkles, FileText, Target } from "lucide-react"
import Link from "next/link"
import { APP_CONFIG } from "@/lib/config"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background grid-pattern">
      {/* Header */}
      <header className="border-b border-border/50 backdrop-blur-sm bg-background/80 sticky top-0 z-50">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
                <Sparkles className="w-5 h-5 text-primary-foreground" />
              </div>
              <span className="text-xl font-bold text-foreground">{APP_CONFIG.name}</span>
            </div>
            <Link href="/write">
              <Button variant="outline" className="bg-transparent">
                시작하기
              </Button>
            </Link>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="py-20 px-4">
        <div className="container mx-auto text-center max-w-4xl">
          <Badge variant="secondary" className="mb-6">
            <Sparkles className="w-4 h-4 mr-2" />
            AI 자소서 도우미
          </Badge>
          <h1 className="text-4xl md:text-6xl font-bold text-balance mb-6">
            AI가 도와주는 <span className="text-primary">완벽한 자기소개서</span>
          </h1>
          <p className="text-xl text-muted-foreground text-balance mb-8 leading-relaxed">
            {APP_CONFIG.description}
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/write">
              <Button size="lg" className="glow-effect">
                <FileText className="w-5 h-5 mr-2" />
                지금 시작하기
              </Button>
            </Link>
            <Button variant="outline" size="lg">
              <Target className="w-5 h-5 mr-2" />
              예시 보기
            </Button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 px-4 border-t border-border/50">
        <div className="container mx-auto max-w-6xl">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold mb-4">왜 자소서 AI를 선택해야 할까요?</h2>
            <p className="text-muted-foreground text-lg">전문적이고 효과적인 자기소개서 작성을 위한 AI 도구</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <Card>
              <CardHeader>
                <Target className="w-8 h-8 text-primary mb-2" />
                <CardTitle>맞춤형 최적화</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">회사와 직군에 맞춰 개인화된 자기소개서를 작성합니다.</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <Sparkles className="w-8 h-8 text-primary mb-2" />
                <CardTitle>AI 기반 개선</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">최신 AI 기술로 문장 구조와 내용을 전문적으로 다듬습니다.</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <FileText className="w-8 h-8 text-primary mb-2" />
                <CardTitle>즉시 결과 확인</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">몇 분 안에 완성된 자기소개서를 확인하고 다운로드할 수 있습니다.</p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 px-4">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl font-bold mb-4">지금 바로 시작해보세요</h2>
          <p className="text-muted-foreground text-lg mb-8">
            몇 분만 투자하면 전문적인 자기소개서를 완성할 수 있습니다.
          </p>
          <Link href="/write">
            <Button size="lg" className="glow-effect">
              <Sparkles className="w-5 h-5 mr-2" />
              무료로 시작하기
            </Button>
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-border/50 py-8 px-4">
        <div className="container mx-auto text-center">
          <p className="text-muted-foreground">© 2024 자소서 AI. 모든 권리 보유.</p>
        </div>
      </footer>
    </div>
  )
}
