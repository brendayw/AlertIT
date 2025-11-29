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
public class WindAlertEvaluatorService implements WeatherAlertEvaluator {

    // UMBRALES – Bahía Blanca (Casi todo Prov de Buenos Aires, La Pampa,
    //  Entre Rios, Sur de Santa Fe, Casi todo Cordoba)
    private static final double VIENTO_AMARILLO_SOSTENIDO = 55.0;
    private static final double VIENTO_AMARILLO_RAFAGAS = 65.0;

    private static final double VIENTO_NARANJA_SOSTENIDO = 75.0;
    private static final double VIENTO_NARANJA_RAFAGAS = 90.0;

    private static final double VIENTO_ROJO_SOSTENIDO = 90.0;
    private static final double VIENTO_ROJO_RAFAGAS = 110.0;

    @Override
    public List<Alert> evaluate(WeatherData weather) {
        List<Alert> alerts = new ArrayList<>();

        double viento = weather.getWindSpeed();
        double rafagas = viento * 1.7;

        // ROJO
        if (viento >= VIENTO_ROJO_SOSTENIDO || rafagas >= VIENTO_ROJO_RAFAGAS) {
            alerts.add(new Alert(
                    AlertLevel.ROJO,
                    AlertType.VIENTO,
                    "Ráfagas extremas",
                    "Evite toda actividad en el exterior."
            ));
            return alerts;
        }

        // NARANJA
        if (viento >= VIENTO_NARANJA_SOSTENIDO || rafagas >= VIENTO_NARANJA_RAFAGAS) {
            alerts.add(new Alert(
                    AlertLevel.NARANJA,
                    AlertType.VIENTO,
                    "Ráfagas extremas",
                    "Evite toda actividad en el exterior."
            ));
            return alerts;
        }

        // AMARILLO
        if (viento >= VIENTO_AMARILLO_SOSTENIDO || rafagas >= VIENTO_AMARILLO_RAFAGAS) {
            alerts.add(new Alert(
                    AlertLevel.AMARILLO,
                    AlertType.VIENTO,
                    "Ráfagas extremas",
                    "Evite toda actividad en el exterior."
            ));
        }

        return alerts;
    }
}
