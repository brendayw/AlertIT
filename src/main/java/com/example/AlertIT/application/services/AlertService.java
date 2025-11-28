package com.example.AlertIT.application.services;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherService;
import com.example.AlertIT.domain.services.AlertEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    private final WeatherService weatherService;
    private final AlertEvaluator alertEvaluator;
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    public AlertService(WeatherService weatherService, AlertEvaluator alertEvaluator) {
        this.weatherService = weatherService;
        this.alertEvaluator = alertEvaluator;
    }

    public Map<String, Object> processLocationAlert(String address) {
        logger.info("PROCESANDO ALERTA SMN para: {}", address);

        Map<String, Object> result = new HashMap<>();

        try {
            GeocodingData geocodingData = weatherService.getGeocodingData(address);
            result.put("geocoding", geocodingData);

            if (!geocodingData.isValidLocation()) {
                result.put("nivelAlerta", "ERROR");
                result.put("mensaje", "Ubicación no válida");
                return result;
            }

            String coordinates = geocodingData.getLatitude() + "," + geocodingData.getLongitude();
            WeatherData weather = weatherService.getCurrentWeather(coordinates);
            result.put("clima", weather);

            List<Alert> activeAlerts = alertEvaluator.evaluateWeatherAlerts(weather);
            String overallLevel = alertEvaluator.determineOverallAlertLevel(activeAlerts);

            result.put("nivelAlerta", overallLevel);
            result.put("alertasActivas", activeAlerts);
            result.put("resumen", alertEvaluator.generateAlertSummary(overallLevel, activeAlerts));
            result.put("recomendaciones", alertEvaluator.generateRecommendations(activeAlerts));
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("fuente", "Sistema de Alertas - Criterios SMN");

            logger.info("Evaluación completada. Nivel: {}, Alertas: {}", overallLevel, activeAlerts.size());

        } catch (Exception e) {
            logger.error("Error en el servicio de alertas: {}", e.getMessage());
            result.put("nivelAlerta", "ERROR");
            result.put("mensaje", "Error en el sistema: " + e.getMessage());
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