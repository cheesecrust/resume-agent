package com.resumeai.controller;

import com.resumeai.dto.ResumeGenerationRequest;
import com.resumeai.dto.ResumeGenerationResponse;
import com.resumeai.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://your-frontend-domain.com"})
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/generate-resume")
    public ResponseEntity<ResumeGenerationResponse> generateResume(
            @Valid @RequestBody ResumeGenerationRequest request) {

        log.info("자소서 생성 API 호출 - 회사: {}, 직군: {}",
            request.getCompany(), request.getPosition());

        try {
            ResumeGenerationResponse response = resumeService.generateResume(request);

            if (response.getError() != null) {
                log.error("자소서 생성 실패: {}", response.getError());
                return ResponseEntity.status(500).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("자소서 생성 API 처리 중 오류: ", e);
            return ResponseEntity.status(500).body(
                ResumeGenerationResponse.error("서버 처리 중 오류가 발생했습니다.")
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Resume AI Backend is running!");
    }
}