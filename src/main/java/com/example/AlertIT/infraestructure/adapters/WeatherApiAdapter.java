package com.example.AlertIT.infraestructure.adapters;

import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.WeatherService;
import com.example.AlertIT.infraestructure.client.WeatherAPIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherApiAdapter implements WeatherService {

    private final WeatherAPIClient weatherAPIClient;

    @Override
    public WeatherData getCurrentWeather(String location) {
        return weatherAPIClient.getCurrentWeather(location);
    }

    @Override
    public GeocodingData getGeocodingData(String location) {
        return weatherAPIClient.getGeocodingData(location);
    }
}
