package com.example.AlertIT.domain.ports.outbound;

import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;

public interface WeatherService {
    WeatherData getCurrentWeather(String location);
    GeocodingData getGeocodingData(String location);
}
