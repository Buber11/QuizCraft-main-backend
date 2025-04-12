package main.QuizCraft.mapStruct;

import main.QuizCraft.kafka.ProcessingTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

public class ExpirationConcurrentHashMap extends ConcurrentHashMap<String, ProcessingTask> {

    @Override
    public ProcessingTask get(Object key) {

        super.forEach((k, task) -> {
            if (task.getCompletedAt() != null &&
                    task.getCompletedAt().plus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
                super.remove(k);
            }
        });
        return super.get(key);
    }

}
