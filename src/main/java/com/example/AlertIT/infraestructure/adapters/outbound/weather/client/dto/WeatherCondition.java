package com.example.AlertIT.infraestructure.adapters.outbound.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherCondition {

    @JsonProperty("text")
    private String text;
}
