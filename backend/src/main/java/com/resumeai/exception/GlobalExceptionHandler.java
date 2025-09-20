package com.resumeai.exception;

import com.resumeai.dto.ResumeGenerationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("입력 검증 실패: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResumeGenerationResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.error("잘못된 인자: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(
            ResumeGenerationResponse.error("잘못된 요청입니다: " + ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResumeGenerationResponse> handleGenericException(Exception ex) {
        log.error("예상치 못한 오류 발생: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ResumeGenerationResponse.error("서버에서 오류가 발생했습니다.")
        );
    }
}