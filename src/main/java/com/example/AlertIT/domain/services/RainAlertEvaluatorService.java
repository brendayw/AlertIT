package com.example.AlertIT.domain.services;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.AlertLevel;
import com.example.AlertIT.domain.models.AlertType;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherAlertEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RainAlertEvaluatorService implements WeatherAlertEvaluator {

    // UMBRALES – Bahía Blanca (Prov Buenos Aires y La Pampa)
    private static final double LLUVIA_AMARILLO = 40.0; // mm / 12 h
    private static final double LLUVIA_NARANJA = 70.0; // mm / 12 h
    private static final double LLUVIA_ROJO = 175.0;   // mm / 24 h

    @Override
    public List<Alert> evaluate(WeatherData weather) {
        List<Alert> alerts = new ArrayList<>();

        String condition = weather.getCondition().toLowerCase();
        double humedad = weather.getHumidity();
        double precipitacionEstimada = estimatePrecipitation(condition, humedad);

        // ROJO (175 mm / 24 h)
        if (precipitacionEstimada >= LLUVIA_ROJO) {
            alerts.add(new Alert(
                    AlertLevel.ROJO,
                    AlertType.LLUVIA,
                    String.format("Precipitación extrema estimada: %.0f mm/24h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_ROJO),
                    "Inundaciones severas. Busque zonas altas y evite circular."
            ));

        } else if (precipitacionEstimada >= LLUVIA_NARANJA) {
            alerts.add(new Alert(
                    AlertLevel.NARANJA,
                    AlertType.LLUVIA,
                    String.format("Lluvia muy intensa: %.0f mm/12h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_NARANJA),
                    "Riesgo elevado de anegamientos. Evite zonas bajas."
            ));

        } else if (precipitacionEstimada >= LLUVIA_AMARILLO) {
            alerts.add(new Alert(
                    AlertLevel.AMARILLO,
                    AlertType.LLUVIA,
                    String.format("Lluvia moderada: %.0f mm/12h (Umbral: %.0f mm)",
                            precipitacionEstimada, LLUVIA_AMARILLO),
                    "Lluvias persistentes. Manténgase atento a los partes oficiales."
            ));
        }

        if (condition.contains("tormenta") || condition.contains("storm")) {
            alerts.add(new Alert(
                    AlertLevel.NARANJA,
                    AlertType.TORMENTA,
                    "Actividad tormentosa detectada",
                    "Posibles rayos y ráfagas fuertes. Evite áreas abiertas."
            ));
        }

        return alerts;
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
}
