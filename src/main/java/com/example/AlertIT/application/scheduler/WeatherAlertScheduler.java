package com.example.AlertIT.application.scheduler;

import com.example.AlertIT.infraestructure.adapters.outbound.notification.TwilioNotificationAdapter;
import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.enums.AlertLevel;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.outbound.WeatherService;
import com.example.AlertIT.domain.services.AlertEvaluator;
import com.example.AlertIT.presentation.dto.AlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherAlertScheduler {

    private final WeatherService weatherService;
    private final AlertEvaluator alertEvaluator;
    private final TwilioNotificationAdapter twilioNotificationService;

    @Value("${weather.monitor.location:Bahia Blanca}")
    private String monitorLocation;

    //cada 5 minutos
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void checkAlerts() {
        try {
            log.info("Ejecutando verificación programada para {}", monitorLocation);

            WeatherData weather = weatherService.getCurrentWeather(monitorLocation);
            List<Alert> alerts = alertEvaluator.evaluateWeatherAlerts(weather);
            AlertLevel overall = alertEvaluator.determineOverallAlertLevel(alerts);

            if (overall == AlertLevel.VERDE) {
                log.info("Sin alertas meteorológicas para {}", monitorLocation);
                return;
            }

            log.warn("⚠️ ALERTA detectada: {} para {}", overall, monitorLocation);

            AlertResponse alertResponse = AlertResponse.fromDomain(
                    alerts,
                    overall.name(),
                    alertEvaluator.generateAlertSummary(overall, alerts),
                    alertEvaluator.generateRecommendations(alerts)
            );

            StringBuilder message = new StringBuilder();
            message.append("🚨 *ALERTA METEOROLÓGICA* 🚨\n\n");

            // Alertas activas con icono de color segun nivel
            alertResponse.alertasActivas().forEach(a -> {
                String icono = getIconForLevel(a.nivel().name());

                message.append(icono)
                        .append(" *")
                        .append(a.tipo())        // SIN capitalize
                        .append(":* ")
                        .append(a.descripcion())
                        .append("\n");
            });

            // Recomendaciones
            message.append("\n*Recomendaciones:*\n");
            alertResponse.recomendaciones()
                    .forEach(r -> message.append("• ").append(r).append("\n"));

            // Fecha bonita
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            message.append("\n🕒 ").append(fecha).append("\n");

            // Fuente personalizada
            message.append("Fuente: Sistema de Alertas SMN – Umbrales para la zona de Bahía Blanca");

            twilioNotificationService.send(message.toString());

        } catch (Exception e) {
            log.error("Error durante la verificación de alertas meteorológicas", e);
        }
    }

    private String getIconForLevel(String nivel) {
        if (nivel == null) return "";
        return switch (nivel.toUpperCase()) {
            case "VERDE" -> "🟢";
            case "AMARILLO" -> "🟡";
            case "NARANJA" -> "🟠";
            case "ROJO" -> "🔴";
            default -> "⚪";
        };
    }
}
