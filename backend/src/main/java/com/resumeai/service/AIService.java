package com.resumeai.service;

import com.resumeai.dto.AIModelType;
import com.resumeai.dto.ResumeGenerationRequest;
import com.resumeai.dto.ResumeGenerationResponse;

public interface AIService {
    ResumeGenerationResponse generateResume(ResumeGenerationRequest request);
    boolean isModelSupported(AIModelType modelType);
}