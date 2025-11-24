package com.sentiment.service;

import com.sentiment.model.SentimentResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Сервис для анализа тональности текста (mock-реализация)
 * В продакшене здесь была бы интеграция с ML-моделью
 */
@Service
public class SentimentService {

    private final MeterRegistry meterRegistry;
    private final Counter positiveCounter;
    private final Counter negativeCounter;
    private final Counter neutralCounter;

    public SentimentService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.positiveCounter = Counter.builder("sentiment.positive")
                .description("Count of positive sentiments")
                .register(meterRegistry);
        this.negativeCounter = Counter.builder("sentiment.negative")
                .description("Count of negative sentiments")
                .register(meterRegistry);
        this.neutralCounter = Counter.builder("sentiment.neutral")
                .description("Count of neutral sentiments")
                .register(meterRegistry);
    }

    /**
     * Анализирует тональность текста (mock-реализация)
     * Простая логика на основе ключевых слов
     */
    public SentimentResponse analyzeSentiment(String text) {
        String sentiment;
        double score;

        String lowerText = text.toLowerCase();

        // Простая логика на основе ключевых слов
        if (containsPositiveWords(lowerText)) {
            sentiment = "positive";
            score = 0.8 + Math.random() * 0.2;  // 0.8 - 1.0
            positiveCounter.increment();
        } else if (containsNegativeWords(lowerText)) {
            sentiment = "negative";
            score = -0.8 - Math.random() * 0.2; // -1.0 - -0.8
            negativeCounter.increment();
        } else {
            sentiment = "neutral";
            score = Math.random() * 0.4 - 0.2;  // -0.2 - 0.2
            neutralCounter.increment();
        }

        // Считаем метрику "обработано текстов"
        meterRegistry.counter("sentiment.requests").increment();

        return new SentimentResponse(
                text,
                sentiment,
                Math.round(score * 100.0) / 100.0,  // Округляем до 2 знаков
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    private boolean containsPositiveWords(String text) {
        String[] positiveWords = {"good", "great", "excellent", "awesome", "love", "happy", 
                                 "positive", "best", "wonderful", "fantastic", "хорошо", "отлично",
                                 "прекрасно", "любви", "счастлив"};
        for (String word : positiveWords) {
            if (text.contains(word)) return true;
        }
        return false;
    }

    private boolean containsNegativeWords(String text) {
        String[] negativeWords = {"bad", "terrible", "awful", "hate", "sad", "negative", 
                                 "worst", "horrible", "disgusting", "плохо", "ужасно",
                                 "противно", "ненави", "грустно"};
        for (String word : negativeWords) {
            if (text.contains(word)) return true;
        }
        return false;
    }
}
