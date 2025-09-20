package com.resumeai.service;

import com.resumeai.dto.AIModelType;
import com.resumeai.dto.ResumeGenerationRequest;
import com.resumeai.dto.ResumeGenerationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final OpenAIService openAIService;

    public ResumeGenerationResponse generateResume(ResumeGenerationRequest request) {
        try {
            log.info("자소서 생성 요청 - 회사: {}, 직군: {}, 모델: {}",
                request.getCompany(), request.getPosition(), request.getAiModel());

            AIModelType modelType = AIModelType.fromString(request.getAiModel());

            // 현재는 OpenAI만 지원
            if (!openAIService.isModelSupported(modelType)) {
                log.warn("지원하지 않는 AI 모델: {}", request.getAiModel());
                return ResumeGenerationResponse.error("지원하지 않는 AI 모델입니다.");
            }

            ResumeGenerationResponse response = openAIService.generateResume(request);

            if (response.getError() != null) {
                log.error("자소서 생성 실패: {}", response.getError());
            } else {
                log.info("자소서 생성 성공 - 길이: {} 글자", response.getImprovedResume().length());
            }

            return response;

        } catch (Exception e) {
            log.error("자소서 생성 중 예외 발생: ", e);
            return ResumeGenerationResponse.error("서비스 처리 중 오류가 발생했습니다.");
        }
    }
}