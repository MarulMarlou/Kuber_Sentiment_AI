FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Собираем приложение и пропускаем тесты (для ускорения)
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine

# Метаданные образа
LABEL maintainer="Sentiment AI Team"
LABEL description="Sentiment Analysis API with Spring Boot Actuator for Kubernetes"
LABEL version="1.0.0"

# Рабочая директория
WORKDIR /app

# Копируем JAR из builder стейджа
COPY --from=builder /build/target/sentiment-api-1.0.0.jar app.jar

# Создаём непривилегированного пользователя (безопасность)
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    chown -R appuser:appuser /app

# Переходим на непривилегированного пользователя
USER appuser

# Порт, на котором слушает приложение
EXPOSE 8080

# Health check (Kubernetes будет использовать для проверки)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher -c "curl -f http://localhost:8080/api/health || exit 1" || exit 1

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
