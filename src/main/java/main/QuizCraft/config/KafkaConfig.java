package main.QuizCraft.config;

import main.QuizCraft.kafka.TOPIC;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic newTopic1() {
        return new NewTopic(
                TOPIC.AI_REQUEST_FOR_VECTOR_DATA.getTopicName(),
                1,
                (short) 1);
    }
    @Bean
    public NewTopic newTopic2(){
        return new NewTopic(
                TOPIC.AI_RESPONSE_FOR_VECTOR_DATA.getTopicName(),
                1,
                (short) 1);
    }
    @Bean
    public NewTopic statusProcessing(){
        return new NewTopic(
                TOPIC.STATUS_PROCESSING.getTopicName(),
                1,
                (short) 1);
    }
    @Bean
    public NewTopic textRequest(){
        return new NewTopic(
                TOPIC.TEXT_PROCESSING_REQUEST.getTopicName(),
                1,
                (short) 1);
    }

}
