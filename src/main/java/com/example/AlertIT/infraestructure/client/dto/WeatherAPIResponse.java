package com.example.AlertIT.infraestructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherAPIResponse {

    @JsonProperty("location")
    private WeatherLocation location;

    @JsonProperty("current")
    private WeatherCurrent current;

    @JsonProperty("alerts")
    private WeatherAlerts alerts;
}
