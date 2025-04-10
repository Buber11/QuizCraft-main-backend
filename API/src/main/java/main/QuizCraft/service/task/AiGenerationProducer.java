package main.QuizCraft.service.task;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.kafkaStatus.ProcessingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiGenerationProducer {

        private final KafkaTemplate<String, String> kafkaTemplate;
        private final Logger logger = LoggerFactory.getLogger(AiGenerationProducer.class);

        private static final String TOPIC = "ai-generation-requests";

        @Async
        public void sendMessage(ProcessingTask processingTask) {
                kafkaTemplate.send(TOPIC, processingTask.getTaskId());
                logger.info("Sent message to Kafka: Type={}, taskId={}",processingTask.getMethodProcessingType(), processingTask.getTaskId());
        }
}

