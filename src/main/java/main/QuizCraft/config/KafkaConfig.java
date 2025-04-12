package main.QuizCraft.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic newTopic1() {
        return new NewTopic("ai-request-for-vector-data", 1, (short) 1);
    }

    @Bean NewTopic newTopic2(){
        return new NewTopic("ai-response-for-vector-data", 1, (short) 1);
    }
    @Bean NewTopic newTopic3(){
        return new NewTopic("status-processing", 1, (short) 1);
    }
}
