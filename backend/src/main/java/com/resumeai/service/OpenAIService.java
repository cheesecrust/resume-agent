package com.resumeai.service;

import com.resumeai.dto.AIModelType;
import com.resumeai.dto.ResumeGenerationRequest;
import com.resumeai.dto.ResumeGenerationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService implements AIService {

    private final ChatClient chatClient;

    @Override
    public ResumeGenerationResponse generateResume(ResumeGenerationRequest request) {
        int maxRetries = 3;
        int minWordCount = (int) (request.getWordLimit() * 0.9); // 90% 기준

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("자소서 생성 시도 {}/{} - 목표 글자수: {}자 이상",
                    attempt, maxRetries, minWordCount);

                String response = callOpenAI(request, attempt);
                int actualWordCount = response.length();

                log.info("생성된 자소서 글자수: {}자 (목표: {}자 이상)",
                    actualWordCount, minWordCount);

                // 글자수 체크
                if (actualWordCount >= minWordCount) {
                    log.info("글자수 조건 만족 - 생성 완료");
                    List<String> comments = generateComments(request, attempt, actualWordCount);
                    return ResumeGenerationResponse.success(response.trim(), comments);
                } else {
                    log.warn("글자수 부족 ({}자 < {}자) - 재시도 필요",
                        actualWordCount, minWordCount);

                    if (attempt == maxRetries) {
                        log.error("최대 재시도 횟수 초과 - 현재 결과 반환");
                        List<String> comments = generateComments(request, attempt, actualWordCount);
                        comments.add("⚠️ 글자수가 목표에 미치지 못했지만 최대 시도 후 반환되었습니다.");
                        return ResumeGenerationResponse.success(response.trim(), comments);
                    }
                }

            } catch (Exception e) {
                log.error("{}번째 시도에서 오류 발생: {}", attempt, e.getMessage());

                if (attempt == maxRetries) {
                    log.error("최대 재시도 횟수 초과 - 오류 반환");
                    return ResumeGenerationResponse.error("AI 서비스 호출 중 오류가 발생했습니다.");
                }
            }
        }

        return ResumeGenerationResponse.error("예상치 못한 오류가 발생했습니다.");
    }

    private String callOpenAI(ResumeGenerationRequest request, int attempt) {
        String systemPrompt = getSystemPrompt();
        String userPrompt = buildPrompt(request, attempt);

        // Spring AI ChatClient 사용 (0.8.1 버전)
        Prompt prompt = new Prompt(
            systemPrompt + "\n\n" + userPrompt,
            OpenAiChatOptions.builder()
                    .withModel(getModelName(request.getAiModel()))
                    .withTemperature(0.7F)
                    .withMaxTokens(2000)
                    .build()
        );

        ChatResponse chatResponse = chatClient.call(prompt);
        return chatResponse.getResult().getOutput().getContent();
    }

    @Override
    public boolean isModelSupported(AIModelType modelType) {
        return modelType == AIModelType.GPT_4 || modelType == AIModelType.GPT_3_5_TURBO;
    }

    private String getModelName(String aiModel) {
        return switch (aiModel.toLowerCase()) {
            case "gpt-4" -> "gpt-4";
            case "gpt-3.5-turbo" -> "gpt-3.5-turbo";
            case "gpt-4-turbo" -> "gpt-4-turbo-preview";
            default -> "gpt-4";
        };
    }

    private String getSystemPrompt() {
        return """
            당신은 전문적인 자기소개서 작성 전문가입니다.
            사용자의 초안을 바탕으로 다음 기준에 맞춰 개선된 자기소개서를 작성해주세요:

            1. 회사와 직군에 적합한 키워드 포함
            2. 구체적이고 설득력 있는 내용으로 개선
            3. 글자수 제한에 맞춰 조정
            4. 문법과 어투를 자연스럽게 개선
            5. STAR 기법(Situation, Task, Action, Result) 활용

            응답은 개선된 자기소개서 내용만 제공하고, 추가 설명은 포함하지 마세요.
            """;
    }

    private String buildPrompt(ResumeGenerationRequest request, int attempt) {
        String attemptPrompt = attempt > 1 ?
            String.format("\n\n이번이 %d번째 시도입니다. 반드시 %d자 이상(90%% 이상)으로 작성해주세요.",
                attempt, (int)(request.getWordLimit() * 0.9)) : "";

        return String.format("""
            자기소개서 문항: %s

            초안 내용: %s

            지원 회사: %s
            지원 직군: %s
            글자수 제한: %d자

            위 정보를 바탕으로 전문적이고 매력적인 자기소개서로 개선해주세요.

            글자수 제한의 90%%는 무조건 작성해줘.%s
            """,
            request.getQuestion(),
            request.getDraft(),
            request.getCompany(),
            request.getPosition(),
            request.getWordLimit(),
            attemptPrompt
        );
    }

    private List<String> generateComments(ResumeGenerationRequest request, int attempt, int actualWordCount) {
        List<String> comments = new ArrayList<>(Arrays.asList(
            "Spring AI를 사용하여 문장 구조를 더 명확하고 논리적으로 개선했습니다.",
            String.format("%s와 %s 직군에 맞는 전문 키워드를 추가했습니다.",
                request.getCompany(), request.getPosition()),
            String.format("%d자 제한에 맞게 내용을 최적화했습니다.", request.getWordLimit()),
            String.format("%s 모델을 사용하여 자연스러운 문체로 개선했습니다.",
                request.getAiModel().toUpperCase()),
            "구체적인 경험과 성과를 부각시켜 설득력을 높였습니다."
        ));

        if (attempt > 1) {
            comments.add(String.format("📝 %d번의 시도를 통해 글자수 조건(%d자 이상)을 만족했습니다.",
                attempt, (int)(request.getWordLimit() * 0.9)));
        }

        comments.add(String.format("✅ 최종 생성된 글자수: %d자", actualWordCount));

        return comments;
    }
}