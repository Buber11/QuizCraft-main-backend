package main.QuizCraft.kafka;

public enum MethodProcessingType {
    FLASHCARD_PROCESSING("FLASHCARD_PROCESSING"),
    QUIZ_PROCESSING("QUIZ_PROCESSING"),
    SUMMARY_PROCESSING("SUMMARY_PROCESSING"),
    TRUE_FALSE_PROCESSING("TRUE_FALSE_PROCESSING"),
    FILL_IN_THE_BLANK_PROCESSING("FILL_IN_THE_BLANK_PROCESSING"),
    VECTOR_PROCESSING("VECTOR_PROCESSING"),;

    private final String type;

    MethodProcessingType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
