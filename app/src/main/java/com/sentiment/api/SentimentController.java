package com.sentiment.api;

import com.sentiment.model.SentimentResponse;
import com.sentiment.service.SentimentService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST контроллер для анализа тональности
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * GET /api/sentiment?text=hello
     * Анализирует тональность переданного текста
     */
    @GetMapping("/sentiment")
    public ResponseEntity<SentimentResponse> analyzeSentiment(@RequestParam String text) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        SentimentResponse response = sentimentService.analyzeSentiment(text);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/sentiment
     * Анализирует тональность через POST с JSON телом
     */
    @PostMapping("/sentiment")
    public ResponseEntity<SentimentResponse> analyzeSentimentPost(@RequestBody Map<String, String> request) {
        String text = request.get("text");

        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        SentimentResponse response = sentimentService.analyzeSentiment(text);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/health
     * Проверка здоровья приложения
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Sentiment Analysis API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/metrics
     * Получить базовую статистику
     */
    @GetMapping("/metrics-summary")
    public ResponseEntity<Map<String, Object>> metricsSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total_requests", meterRegistry.counter("sentiment.requests").count());
        summary.put("positive_count", meterRegistry.counter("sentiment.positive").count());
        summary.put("negative_count", meterRegistry.counter("sentiment.negative").count());
        summary.put("neutral_count", meterRegistry.counter("sentiment.neutral").count());
        return ResponseEntity.ok(summary);
    }
}
