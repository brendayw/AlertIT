package com.example.AlertIT.infraestructure.adapters.outbound.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherAlerts {

    @JsonProperty("alert")
    private List<WeatherAlertItem> alert;
}
