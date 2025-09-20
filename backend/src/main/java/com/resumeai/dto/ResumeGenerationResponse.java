package com.resumeai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeGenerationResponse {

    private String improvedResume;
    private List<String> comments;
    private String error;

    public static ResumeGenerationResponse success(String improvedResume, List<String> comments) {
        return new ResumeGenerationResponse(improvedResume, comments, null);
    }

    public static ResumeGenerationResponse error(String error) {
        return new ResumeGenerationResponse(
            "죄송합니다. 자소서 생성 중 오류가 발생했습니다. 다시 시도해주세요.",
            List.of(),
            error
        );
    }
}