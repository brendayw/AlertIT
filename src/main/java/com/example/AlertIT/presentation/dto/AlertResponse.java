package com.example.AlertIT.presentation.dto;

import com.example.AlertIT.domain.models.Alert;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public record AlertResponse(
        @JsonProperty("nivelAlerta") String nivelAlerta,
        @JsonProperty("alertasActivas") List<AlertItemResponse> alertasActivas,
        @JsonProperty("resumen") String resumen,
        @JsonProperty("recomendaciones") List<String> recomendaciones
) {

    public static AlertResponse fromDomain(List<Alert> alerts, String nivelGeneral, String resumen,
                                           List<String> recomendaciones) {
        List<AlertItemResponse> alertItems = alerts.stream()
                .map(alert -> new AlertItemResponse(
                        alert.getNivel(),
                        alert.getTipo(),
                        alert.getDescripcion(),
                        alert.getRecomendacion()
                ))
                .collect(Collectors.toList());

        return new AlertResponse(nivelGeneral, alertItems, resumen, recomendaciones);
    }
}

