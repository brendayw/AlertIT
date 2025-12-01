package com.example.AlertIT.infraestructure.adapters.outbound.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WeatherAPIResponse {

    @JsonProperty("location")
    private WeatherLocation location;

    @JsonProperty("current")
    private WeatherCurrent current;

    @JsonProperty("alerts")
    private WeatherAlerts alerts;


    public boolean hasAlerts() {
        return alerts != null && alerts.getAlert() != null && !alerts.getAlert().isEmpty();
    }

    public String getAlertMessage() {
        if (hasAlerts()) {
            WeatherAlertItem firstAlert = alerts.getAlert().get(0);
            return String.format("[%s] %s: %s",
                    firstAlert.getSeverity(),
                    firstAlert.getEffective(),
                    firstAlert.getHeadline());
        }
        return "No hay alertas activas";
    }
}
