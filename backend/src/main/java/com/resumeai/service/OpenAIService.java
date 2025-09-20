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

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService implements AIService {

    private final ChatClient chatClient;

    @Override
    public ResumeGenerationResponse generateResume(ResumeGenerationRequest request) {
        try {
            String systemPrompt = getSystemPrompt();
            String userPrompt = buildPrompt(request);

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
            String response = chatResponse.getResult().getOutput().getContent();

            List<String> comments = generateComments(request);

            return ResumeGenerationResponse.success(response.trim(), comments);

        } catch (Exception e) {
            log.error("Spring AI OpenAI 호출 중 오류 발생: ", e);
            return ResumeGenerationResponse.error("AI 서비스 호출 중 오류가 발생했습니다.");
        }
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

    private String buildPrompt(ResumeGenerationRequest request) {
        return String.format("""
            자기소개서 문항: %s

            초안 내용: %s

            지원 회사: %s
            지원 직군: %s
            글자수 제한: %d자

            위 정보를 바탕으로 전문적이고 매력적인 자기소개서로 개선해주세요.
            """,
            request.getQuestion(),
            request.getDraft(),
            request.getCompany(),
            request.getPosition(),
            request.getWordLimit()
        );
    }

    private List<String> generateComments(ResumeGenerationRequest request) {
        return Arrays.asList(
            "Spring AI를 사용하여 문장 구조를 더 명확하고 논리적으로 개선했습니다.",
            String.format("%s와 %s 직군에 맞는 전문 키워드를 추가했습니다.",
                request.getCompany(), request.getPosition()),
            String.format("%d자 제한에 맞게 내용을 최적화했습니다.", request.getWordLimit()),
            String.format("%s 모델을 사용하여 자연스러운 문체로 개선했습니다.",
                request.getAiModel().toUpperCase()),
            "구체적인 경험과 성과를 부각시켜 설득력을 높였습니다."
        );
    }
}