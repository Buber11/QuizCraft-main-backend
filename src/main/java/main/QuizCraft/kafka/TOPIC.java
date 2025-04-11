package main.QuizCraft.kafka;

public enum TOPIC {

    AI_GENERATION_REQUESTS("ai-generation-requests");

    private final String topicName;

    TOPIC(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
