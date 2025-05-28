package main.QuizCraft.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    /**
     * This class is used to create a rate limit configuration for the application.
     * It uses Bucket4j to create a bucket with a limit of 10 requests per minute.
     * The bucket is stored in a concurrent hash map, keyed by the user's IP address.
     */
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    public Bucket resolveBucket(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }
}
