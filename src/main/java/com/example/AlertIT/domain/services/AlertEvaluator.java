package com.example.AlertIT.domain.services;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.WeatherData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertEvaluator {

    // UMBRALES OFICIALES SMN
    private static final double LLUVIA_AMARILLO = 30.0;
    private static final double LLUVIA_NARANJA = 60.0;
    private static final double LLUVIA_ROJO = 125.0;

    private static final double VIENTO_AMARILLO = 65.0;
    private static final double VIENTO_NARANJA = 110.0;
    private static final double VIENTO_ROJO = 150.0;

    private static final double TEMP_CALOR_EXTREMO = 40.0;
    private static final double TEMP_FRIO_EXTREMO = -5.0;

    public List<Alert> evaluateWeatherAlerts(WeatherData weather) {
        List<Alert> alerts = new ArrayList<>();

        evaluateWindAlerts(weather, alerts);
        evaluateRainAlerts(weather, alerts);
        evaluateTemperatureAlerts(weather, alerts);

        return alerts;
    }

    private void evaluateWindAlerts(WeatherData weather, List<Alert> alerts) {
        double velocidadViento = weather.getWindSpeed();
        double estimacionRafagas = velocidadViento * 1.7;

        if (estimacionRafagas >= VIENTO_ROJO) {
            alerts.add(new Alert("ROJO", "VIENTO",
                    String.format("Ráfagas intensas: %.1f km/h (Umbral: %.0f km/h)",
                            estimacionRafagas, VIENTO_ROJO),
                    "Evite actividades al aire libre. Riesgo estructural."));
        }
        else if (estimacionRafagas >= VIENTO_NARANJA) {
            alerts.add(new Alert("NARANJA", "VIENTO",
                    String.format("Ráfagas fuertes: %.1f km/h (Umbral: %.0f km/h)",
                            estimacionRafagas, VIENTO_NARANJA),
                    "Precaución. Objetos sueltos pueden volar."));
        }
        else if (velocidadViento >= VIENTO_AMARILLO) {
            alerts.add(new Alert("AMARILLO", "VIENTO",
                    String.format("Viento sostenido: %.1f km/h (Umbral: %.0f km/h)",
                            velocidadViento, VIENTO_AMARILLO),
                    "Manténgase informado. Vientos moderados a fuertes."));
        }
    }

    private void evaluateRainAlerts(WeatherData weather, List<Alert> alerts) {
        String condicion = weather.getCondition().toLowerCase();
        double humedad = weather.getHumidity();
        double precipitacionEstimada = estimatePrecipitation(condicion, humedad);

        if (precipitacionEstimada >= LLUVIA_ROJO) {
            alerts.add(new Alert("ROJO", "LLUVIA",
                    String.format("Precipitación extrema: %.0f mm/24h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_ROJO),
                    "Inundaciones inminentes. Busque refugio en zonas altas."));
        }
        else if (precipitacionEstimada >= LLUVIA_NARANJA) {
            alerts.add(new Alert("NARANJA", "LLUVIA",
                    String.format("Lluvia intensa: %.0f mm/12h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_NARANJA),
                    "Riesgo de inundaciones. Evite zonas bajas."));
        }
        else if (precipitacionEstimada >= LLUVIA_AMARILLO) {
            alerts.add(new Alert("AMARILLO", "LLUVIA",
                    String.format("Lluvia moderada: %.0f mm/12h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_AMARILLO),
                    "Lluvias persistentes. Posibles acumulaciones."));
        }

        // Alertas por tipo de condición
        if (condicion.contains("tormenta") || condicion.contains("storm")) {
            alerts.add(new Alert("NARANJA", "TORMENTA",
                    "Actividad tormentosa detectada",
                    "Posibles rayos y ráfagas fuertes. Evite áreas abiertas."));
        }
    }

    private void evaluateTemperatureAlerts(WeatherData weather, List<Alert> alerts) {
        double temperatura = weather.getTemperature();

        if (temperatura >= TEMP_CALOR_EXTREMO) {
            alerts.add(new Alert("NARANJA", "CALOR_EXTREMO",
                    String.format("Temperatura extrema: %.1f°C", temperatura),
                    "Riesgo de golpe de calor. Hidrátese y evite exposición solar."));
        }
        else if (temperatura <= TEMP_FRIO_EXTREMO) {
            alerts.add(new Alert("NARANJA", "FRIO_EXTREMO",
                    String.format("Temperatura bajo cero: %.1f°C", temperatura),
                    "Riesgo de hipotermia. Abríguese adecuadamente."));
        }
    }

    private double estimatePrecipitation(String condition, double humidity) {
        if (condition.contains("heavy rain") || condition.contains("lluvia intensa")) {
            return 70.0 + (humidity / 100 * 30);
        }
        if (condition.contains("rain") || condition.contains("lluvia")) {
            return 20.0 + (humidity / 100 * 40);
        }
        if (condition.contains("storm") || condition.contains("tormenta")) {
            return 50.0 + (humidity / 100 * 50);
        }
        return 0.0;
    }

    public String determineOverallAlertLevel(List<Alert> alerts) {
        boolean hasRed = alerts.stream().anyMatch(a -> a.getNivel().equals("ROJO"));
        boolean hasOrange = alerts.stream().anyMatch(a -> a.getNivel().equals("NARANJA"));
        boolean hasYellow = alerts.stream().anyMatch(a -> a.getNivel().equals("AMARILLO"));

        if (hasRed) return "ROJO";
        if (hasOrange) return "NARANJA";
        if (hasYellow) return "AMARILLO";
        return "VERDE";
    }

    public String generateAlertSummary(String overallLevel, List<Alert> alerts) {
        switch (overallLevel) {
            case "ROJO":
                return "🚨 ALERTA ROJA: Condiciones meteorológicas peligrosas.";
            case "NARANJA":
                return "🔶 ALERTA NARANJA: Condiciones meteorológicas adversas.";
            case "AMARILLO":
                return "⚠️ ALERTA AMARILLA: Condiciones que requieren atención.";
            case "VERDE":
                return "✅ SIN ALERTAS: Condiciones dentro de parámetros normales.";
            default:
                return "ℹ️ Situación meteorológica normal.";
        }
    }

    public List<String> generateRecommendations(List<Alert> alerts) {
        List<String> recommendations = new ArrayList<>();

        for (Alert alert : alerts) {
            if (alert.getNivel().equals("ROJO")) {
                recommendations.add("🔴 " + alert.getRecomendacion());
            } else if (alert.getNivel().equals("NARANJA")) {
                recommendations.add("🟠 " + alert.getRecomendacion());
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Condiciones normales. Manténgase informado.");
        }

        return recommendations;
    }
}
