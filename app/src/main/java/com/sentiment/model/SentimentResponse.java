package com.sentiment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель для ответа с результатом анализа тональности
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponse {
    private String text;
    private String sentiment;        // positive, negative, neutral
    private double score;             // -1.0 до 1.0
    private String timestamp;
}
