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
        int maxWordCount = request.getWordLimit(); // 최대 글자수

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("자소서 생성 시도 {}/{} - 목표 글자수: {}자~{}자",
                    attempt, maxRetries, minWordCount, maxWordCount);

                String response = callOpenAI(request, attempt);
                int actualWordCount = response.length();

                log.info("생성된 자소서 글자수: {}자 (목표: {}자~{}자)",
                    actualWordCount, minWordCount, maxWordCount);

                // 글자수 체크
                if (actualWordCount >= minWordCount && actualWordCount <= maxWordCount) {
                    log.info("글자수 조건 만족 - 생성 완료");
                    List<String> comments = generateComments(request, attempt, actualWordCount);
                    return ResumeGenerationResponse.success(response.trim(), comments);
                } else if (actualWordCount < minWordCount) {
                    log.warn("글자수 부족 ({}자 < {}자) - 재시도 필요",
                        actualWordCount, minWordCount);
                    request.setDraft(response.trim());

                    if (attempt == maxRetries) {
                        log.error("최대 재시도 횟수 초과 - 현재 결과 반환");
                        List<String> comments = generateComments(request, attempt, actualWordCount);
                        comments.add("⚠️ 글자수가 목표에 미치지 못했지만 최대 시도 후 반환되었습니다.");
                        return ResumeGenerationResponse.success(response.trim(), comments);
                    }
                } else if (actualWordCount > maxWordCount) {
                    log.warn("글자수 초과 ({}자 > {}자) - 요약 요청",
                        actualWordCount, maxWordCount);
                    request.setDraft(response.trim());

                    if (attempt == maxRetries) {
                        log.error("최대 재시도 횟수 초과 - 현재 결과 반환");
                        List<String> comments = generateComments(request, attempt, actualWordCount);
                        comments.add("⚠️ 글자수가 제한을 초과했지만 최대 시도 후 반환되었습니다.");
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
                    .withTemperature(attempt > 1 ? 0.3F : 0.5F)  // 재시도시 더 일관성 있게
                    .withMaxTokens(3000)  // 더 긴 응답 허용
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
            당신은 글자수 준수에 매우 엄격한 전문 자기소개서 작성 전문가입니다.

            핵심 원칙:
            1. 요청된 글자수를 반드시 지켜야 합니다 (90% 이상 필수)
            2. 글자수가 부족하면 구체적인 경험과 성과를 추가하세요
            3. STAR 기법으로 상황-행동-결과를 상세히 서술하세요
            4. 회사와 직군에 맞는 전문 키워드를 자연스럽게 포함하세요
            5. 문법과 어투를 자연스럽게 개선하세요

            중요: 글자수가 목표에 미달하면 안 됩니다. 반드시 충분한 분량으로 작성하세요.
            응답은 개선된 자기소개서 내용만 제공하고, 추가 설명은 포함하지 마세요.
            """;
    }

    private String buildPrompt(ResumeGenerationRequest request, int attempt) {
        int targetMinLength = (int) (request.getWordLimit() * 0.9);
        int targetMaxLength = request.getWordLimit();

        if (attempt > 1) {
            int currentLength = request.getDraft().length();

            // 글자수 부족시 확장 요청
            if (currentLength < targetMinLength) {
                int needMore = targetMinLength - currentLength;
                log.info("재시도 - 확장 필요: 현재 {}자, 목표 {}자, {}자 더 필요", currentLength, targetMinLength, needMore);

                return String.format("""
                    다음 자기소개서를 정확히 %d자 이상 %d자 이하로 확장해주세요.

                    현재 내용: %s
                    (현재 %d자)

                    요구사항:
                    1. 기존 내용의 핵심은 유지하되, 구체적인 세부사항을 추가하세요
                    2. STAR 기법을 활용해 상황(Situation), 행동(Action), 결과(Result)를 더 자세히 서술하세요
                    3. 수치나 구체적 성과가 있다면 더 상세히 기술하세요
                    4. 반드시 %d자 이상 %d자 이하로 작성하세요
                    5. 불필요한 반복이나 장황한 표현은 피하세요

                    목표 글자수: %d자~%d자 (현재보다 %d자 더 필요)
                    """,
                    targetMinLength,
                    targetMaxLength,
                    request.getDraft(),
                    currentLength,
                    targetMinLength,
                    targetMaxLength,
                    targetMinLength,
                    targetMaxLength,
                    needMore
                );
            }
            // 글자수 초과시 요약 요청
            else if (currentLength > targetMaxLength) {
                int needLess = currentLength - targetMaxLength;
                log.info("재시도 - 요약 필요: 현재 {}자, 목표 {}자, {}자 줄여야 함", currentLength, targetMaxLength, needLess);

                return String.format("""
                    다음 자기소개서를 정확히 %d자 이하로 요약해주세요.

                    현재 내용: %s
                    (현재 %d자)

                    요구사항:
                    1. 핵심 메시지와 중요한 성과는 반드시 유지하세요
                    2. 중복되거나 부차적인 내용을 제거하세요
                    3. 문장을 더 간결하고 명확하게 수정하세요
                    4. 반드시 %d자 이상 %d자 이하로 작성하세요
                    5. 전체적인 논리 구조는 유지하세요

                    목표 글자수: %d자~%d자 (현재보다 %d자 줄여야 함)
                    """,
                    targetMaxLength,
                    request.getDraft(),
                    currentLength,
                    targetMinLength,
                    targetMaxLength,
                    targetMinLength,
                    targetMaxLength,
                    needLess
                );
            }
        }

        return String.format("""
            자기소개서 문항: %s

            초안 내용: %s

            지원 회사: %s
            지원 직군: %s

            위 정보를 바탕으로 전문적이고 매력적인 자기소개서로 개선해주세요.

            중요한 요구사항:
            1. 반드시 %d자 이상 %d자 이하로 작성하세요
            2. 두괄식으로 문단마다 주제가 되는 말을 앞에다 배치해주세요
            3. STAR 기법(Situation, Task, Action, Result)을 활용하세요
            4. 구체적인 경험과 성과를 포함하세요
            5. %s와 %s 분야에 적합한 키워드를 자연스럽게 포함하세요
            6. 글자수를 정확히 지켜주세요

            글자수 확인: 최종 결과물이 %d자~%d자 범위에 있는지 반드시 확인하고 작성하세요.
            """,
            request.getQuestion(),
            request.getDraft(),
            request.getCompany(),
            request.getPosition(),
            targetMinLength,
            targetMaxLength,
            request.getCompany(),
            request.getPosition(),
            targetMinLength,
            targetMaxLength
        );
    }

    private List<String> generateComments(ResumeGenerationRequest request, int attempt, int actualWordCount) {
        int minTarget = (int) (request.getWordLimit() * 0.9);
        int maxTarget = request.getWordLimit();

        List<String> comments = new ArrayList<>(Arrays.asList(
            "Spring AI를 사용하여 문장 구조를 더 명확하고 논리적으로 개선했습니다.",
            String.format("%s와 %s 직군에 맞는 전문 키워드를 추가했습니다.",
                request.getCompany(), request.getPosition()),
            String.format("%d자 제한에 맞게 내용을 최적화했습니다.", maxTarget),
            String.format("%s 모델을 사용하여 자연스러운 문체로 개선했습니다.",
                request.getAiModel().toUpperCase()),
            "구체적인 경험과 성과를 부각시켜 설득력을 높였습니다."
        ));

        if (attempt > 1) {
            // 초기 draft 길이 확인
            int draftLength = request.getDraft().length();
            String action = draftLength < minTarget ? "확장" : "요약";
            comments.add(String.format("📝 %d번의 시도를 통해 글자수 조건(%d자~%d자)에 맞게 %s했습니다.",
                attempt, minTarget, maxTarget, action));
        }

        // 최종 글자수 상태 표시
        String status;
        if (actualWordCount >= minTarget && actualWordCount <= maxTarget) {
            status = "✅ 목표 범위 달성";
        } else if (actualWordCount < minTarget) {
            status = "⚠️ 목표보다 부족";
        } else {
            status = "⚠️ 목표를 초과";
        }

        comments.add(String.format("%s - 최종 글자수: %d자 (목표: %d자~%d자)",
            status, actualWordCount, minTarget, maxTarget));

        return comments;
    }
}