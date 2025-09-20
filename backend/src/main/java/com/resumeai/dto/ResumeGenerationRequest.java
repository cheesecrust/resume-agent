package com.resumeai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResumeGenerationRequest {

    @NotBlank(message = "자소서 문항은 필수입니다.")
    @Size(max = 1000, message = "자소서 문항은 1000자를 초과할 수 없습니다.")
    private String question;

    @NotBlank(message = "초안은 필수입니다.")
    @Size(max = 5000, message = "초안은 5000자를 초과할 수 없습니다.")
    private String draft;

    @NotNull(message = "글자수 제한은 필수입니다.")
    @Positive(message = "글자수 제한은 양수여야 합니다.")
    private Integer wordLimit;

    @NotBlank(message = "회사명은 필수입니다.")
    @Size(max = 100, message = "회사명은 100자를 초과할 수 없습니다.")
    private String company;

    @NotBlank(message = "직군/직무는 필수입니다.")
    @Size(max = 100, message = "직군/직무는 100자를 초과할 수 없습니다.")
    private String position;

    private String aiModel = "gpt-4";

    private Boolean includeComments = true;
}