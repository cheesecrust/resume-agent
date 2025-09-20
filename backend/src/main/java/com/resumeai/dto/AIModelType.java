package com.resumeai.dto;

public enum AIModelType {
    GPT_4("gpt-4"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    CLAUDE_3("claude-3"),
    GEMINI_PRO("gemini-pro");

    private final String modelName;

    AIModelType(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public static AIModelType fromString(String modelName) {
        for (AIModelType type : AIModelType.values()) {
            if (type.modelName.equalsIgnoreCase(modelName)) {
                return type;
            }
        }
        return GPT_4; // 기본값
    }
}