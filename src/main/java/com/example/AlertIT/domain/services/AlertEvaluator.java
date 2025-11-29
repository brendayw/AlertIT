package com.example.AlertIT.domain.services;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.AlertLevel;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherAlertEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlertEvaluator {

    private final List<WeatherAlertEvaluator> evaluators;

    public List<Alert> evaluateWeatherAlerts(WeatherData weather) {
        List<Alert> alerts = new ArrayList<>();
        evaluators.forEach(evaluator -> alerts.addAll(evaluator.evaluate(weather)));
        return alerts;
    }

    public AlertLevel determineOverallAlertLevel(List<Alert> alerts) {
        return alerts.stream()
                .map(Alert::getNivel)
                .max(Comparator.comparingInt(AlertLevel::getSeverityValue))
                .orElse(AlertLevel.VERDE);
    }

    public String generateAlertSummary(AlertLevel overall, List<Alert> alerts) {
        if (alerts.isEmpty()) {
            return "Sin alertas meteorológicas activas.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Nivel general: ").append(overall.name()).append("\n");

        for (Alert a : alerts) {
            sb.append("- ")
                    .append(a.getTipo()).append(" → ")
                    .append(a.getNivel()).append(": ")
                    .append(a.getDescripcion()).append("\n");
        }

        return sb.toString();
    }

    public List<String> generateRecommendations(List<Alert> alerts) {
        return alerts.stream()
                .map(Alert::getRecomendacion)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
