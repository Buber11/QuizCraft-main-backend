package main.QuizCraft.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaProducer {

        private final KafkaTemplate<String, String> kafkaTemplate;
        private final ObjectMapper objectMapper;
        private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);


        @Async
        public void sendMessage(ProcessingTask processingTask, TOPIC topic) {
                if (topic == null) {
                        logger.error("Invalid topic: null");
                        throw new IllegalArgumentException("Topic cannot be null");
                }
                try {
                        String message = objectMapper.writeValueAsString(processingTask);
                        kafkaTemplate.send(topic.getTopicName(), message);
                        logger.info("Sent message to Kafka: Type={}, taskId={}", processingTask.getMethodProcessingType(), processingTask.getTaskId());
                } catch (JsonProcessingException e) {
                        logger.error("Error serializing ProcessingTask to JSON: {}", e.getMessage(), e);
                }
        }
}

