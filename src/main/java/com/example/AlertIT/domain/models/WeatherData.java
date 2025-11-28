package com.example.AlertIT.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherData {
    private Double temperature;
    private String condition;
    private String location;
    private Double humidity;
    private Double windSpeed;

    //methods
    public boolean isExtremeTemperature() {
        return temperature != null && (temperature > 35 || temperature < 0);
    }
}
