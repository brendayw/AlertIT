package com.example.AlertIT.infraestructure.adapters.outbound.weather;

import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.domain.ports.outbound.WeatherService;
import com.example.AlertIT.infraestructure.adapters.outbound.weather.client.WeatherAPIClient;
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
