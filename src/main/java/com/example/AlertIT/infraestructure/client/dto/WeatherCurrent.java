package com.example.AlertIT.infraestructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherCurrent {

    @JsonProperty("temp_c")
    private double tempC;

    @JsonProperty("condition")
    private WeatherCondition condition;

    @JsonProperty("humidity")
    private double humidity;

    @JsonProperty("wind_kph")
    private double windKph;
}
