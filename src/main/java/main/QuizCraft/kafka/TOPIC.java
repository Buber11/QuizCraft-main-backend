package main.QuizCraft.kafka;

public enum TOPIC {

    AI_REQUEST_FOR_VECTOR_DATA("ai-request-for-vector-data"),
    AI_RESPONSE_FOR_VECTOR_DATA("ai-response-for-vector-data"),
    STATUS_PROCESSING("status-processing"),
    TEXT_PROCESSING("text-processing");

    private final String topicName;

    TOPIC(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
