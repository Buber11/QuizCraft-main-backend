package main.QuizCraft.map;

import main.QuizCraft.kafka.ProcessingTask;

import java.time.ZonedDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * A thread-safe, self-cleaning map for storing {@link ProcessingTask} objects
 * with automatic expiration based on their {@code completedAt} timestamps.
 *
 * <p>This map supports the following expiration strategies:
 * <ul>
 *   <li><strong>Scheduled Cleanup:</strong> Periodically scans and removes expired tasks.</li>
 *   <li><strong>Adaptive Cleanup Frequency:</strong> Adjusts the cleanup interval based on
 *       the number of expired tasks removed in the last cycle.</li>
 *   <li><strong>Lazy Eviction on Access:</strong> Tasks are also checked for expiration
 *       during read operations (via {@code get()}).</li>
 *   <li><strong>Conditional Cleanup on Put:</strong> Triggers immediate cleanup if the map
 *       grows too large (e.g., more than 10,000 entries).</li>
 * </ul>
 *
 * <p>A task is considered expired if:
 * {@code completedAt + 5 minutes < now}
 *
 * <p>This class is useful for building temporary caches or queues where completed tasks
 * should be automatically removed after a fixed TTL (Time To Live).
 *
 * <p>Call {@link #shutdown()} to stop the background cleanup thread if the map is no longer needed.
 */
public class AdaptiveExpiringTaskMap {
    private final ConcurrentHashMap<String, ProcessingTask> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> cleanupFuture;
    private final AtomicInteger recentRemovals = new AtomicInteger(0);

    public AdaptiveExpiringTaskMap() {
        scheduleCleanup(1, TimeUnit.MINUTES);
    }

    public void put(String key, ProcessingTask task) {
        map.put(key, task);
        maybeTriggerManualCleanup();
    }

    public ProcessingTask get(String key) {
        ProcessingTask task = map.get(key);

        if (task != null && task.getCompletedAt() != null &&
                task.getCompletedAt().plusMinutes(5).isBefore(ZonedDateTime.now())) {
            map.remove(key);
            return null;
        }

        return task;
    }

    private void maybeTriggerManualCleanup() {
        if (map.size() > 10_000) {
            cleanup();
        }
    }

    private void scheduleCleanup(long interval, TimeUnit unit) {
        if (cleanupFuture != null && !cleanupFuture.isCancelled()) {
            cleanupFuture.cancel(false);
        }

        cleanupFuture = scheduler.scheduleAtFixedRate(() -> {
            int removed = cleanup();
            adjustCleanupRate(removed);
        }, 0, interval, unit);
    }

    private int cleanup() {
        ZonedDateTime now = ZonedDateTime.now();
        AtomicInteger removedCount = new AtomicInteger(0);

        map.entrySet().removeIf(entry -> {
            ZonedDateTime completed = entry.getValue().getCompletedAt();
            if (completed != null && completed.plusMinutes(5).isBefore(now)) {
                removedCount.incrementAndGet();
                return true;
            }
            return false;
        });

        return removedCount.get();
    }

    private void adjustCleanupRate(int removed) {
        recentRemovals.set(removed);
        if (removed > 100) {
            scheduleCleanup(10, TimeUnit.SECONDS);
        } else if (removed == 0) {
            scheduleCleanup(5, TimeUnit.MINUTES);
        } else {
            scheduleCleanup(1, TimeUnit.MINUTES);
        }
    }

    public int size() {
        return map.size();
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}

