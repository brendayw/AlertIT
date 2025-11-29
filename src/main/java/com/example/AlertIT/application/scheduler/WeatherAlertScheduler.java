package com.example.AlertIT.application.scheduler;

import com.example.AlertIT.application.services.TwilioNotificationService;
import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.AlertLevel;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherService;
import com.example.AlertIT.domain.services.AlertEvaluator;
import com.example.AlertIT.presentation.dto.AlertItemResponse;
import com.example.AlertIT.presentation.dto.AlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherAlertScheduler {

    private final WeatherService weatherService;
    private final AlertEvaluator alertEvaluator;
    private final TwilioNotificationService twilioNotificationService;

    @Value("${weather.monitor.location:Bahia Blanca}")
    private String monitorLocation;

    // Cada 6 horas
    //@Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    //cada 1 min
    @Scheduled(fixedRate = 60 * 1000)
    public void checkAlerts() {
        try {
            log.info("⏰ Ejecutando verificación programada para {}", monitorLocation);

            WeatherData weather = weatherService.getCurrentWeather(monitorLocation);
            List<Alert> alerts = alertEvaluator.evaluateWeatherAlerts(weather);
            AlertLevel overall = alertEvaluator.determineOverallAlertLevel(alerts);

            if (overall == AlertLevel.VERDE) {
                log.info("Sin alertas meteorológicas para {}", monitorLocation);
                return;
            }

            log.warn("⚠️ ALERTA detectada: {} para {}", overall, monitorLocation);

            // Crear el DTO directamente desde el dominio
            AlertResponse alertResponse = AlertResponse.fromDomain(
                    alerts,
                    overall.name(),
                    alertEvaluator.generateAlertSummary(overall, alerts),
                    alertEvaluator.generateRecommendations(alerts)
            );

            // Formatear mensaje para WhatsApp
            StringBuilder message = new StringBuilder();
            message.append("🚨 *ALERTA METEOROLÓGICA* 🚨\n\n");
            message.append("*Nivel:* ").append(alertResponse.nivelAlerta()).append("\n");
            message.append("*Resumen:* ").append(alertResponse.resumen()).append("\n\n");
            message.append("*Alertas activas:*\n");
            for (AlertItemResponse a : alertResponse.alertasActivas()) {
                message.append("- [").append(a.nivel()).append("] ")
                        .append(a.tipo()).append(": ")
                        .append(a.descripcion()).append("\n");
            }
            message.append("\n*Recomendaciones:*\n");
            alertResponse.recomendaciones().forEach(r -> message.append("• ").append(r).append("\n"));
            message.append("\n🕒 ").append(LocalDateTime.now()).append("\n");
            message.append("Fuente: Sistema de Alertas Interno");

            twilioNotificationService.send(message.toString());

        } catch (Exception e) {
            log.error("Error durante la verificación de alertas meteorológicas", e);
        }
    }
}
