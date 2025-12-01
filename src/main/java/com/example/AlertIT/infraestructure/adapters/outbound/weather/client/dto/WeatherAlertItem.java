package com.example.AlertIT.infraestructure.adapters.outbound.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherAlertItem {

    @JsonProperty("headline")
    private String headline;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("severity")
    private String severity;

    @JsonProperty("effective")
    private String effective;

    @JsonProperty("expires")
    private String expires;

    @JsonProperty("areas")
    private String areas;
}
