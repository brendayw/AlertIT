package com.example.AlertIT.application.services;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.AlertLevel;
import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherService;
import com.example.AlertIT.domain.services.AlertEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertNotificationService {

    private final WeatherService weatherService;
    private final AlertEvaluator alertEvaluator;

    public Map<String, Object> processLocationAlert(String address) {
        log.info("PROCESANDO ALERTA METEOROLÓGICA para: {}", address);

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. Geocoding
            GeocodingData geocodingData = weatherService.getGeocodingData(address);
            result.put("geocoding", geocodingData);

            if (!geocodingData.isValidLocation()) {
                result.put("nivelAlerta", AlertLevel.VERDE); // Sin riesgos
                result.put("mensaje", "Ubicación no válida");
                return result;
            }

            // 2. Obtener clima
            String coordinates = geocodingData.getLatitude() + "," + geocodingData.getLongitude();
            WeatherData weather = weatherService.getCurrentWeather(coordinates);
            result.put("clima", weather);

            // 3. Evaluar alertas (lluvia, viento, etc.)
            List<Alert> activeAlerts = alertEvaluator.evaluateWeatherAlerts(weather);

            // 4. Determinar nivel máximo entre todas las alertas
            AlertLevel overallLevel = alertEvaluator.determineOverallAlertLevel(activeAlerts);

            // 5. Construir respuesta
            result.put("nivelAlerta", overallLevel);
            result.put("alertasActivas", activeAlerts);
            result.put("resumen", alertEvaluator.generateAlertSummary(overallLevel, activeAlerts));
            result.put("recomendaciones", alertEvaluator.generateRecommendations(activeAlerts));
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("fuente", "Sistema de Alertas - Criterios Bahía Blanca");

            log.info("Evaluación completada. Nivel: {}, Alertas detectadas: {}", overallLevel, activeAlerts.size());

        } catch (Exception e) {
            log.error("Error en el servicio de alertas: {}", e.getMessage(), e);

            result.put("nivelAlerta", AlertLevel.ROJO);
            result.put("mensaje", "Error interno del sistema: " + e.getMessage());
        }

        return result;
    }

    public WeatherData getWeatherForLocation(String location) {
        return weatherService.getCurrentWeather(location);
    }

    public GeocodingData getCoordinatesForAddress(String address) {
        return weatherService.getGeocodingData(address);
    }
}