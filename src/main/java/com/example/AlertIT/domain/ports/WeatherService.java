package com.example.AlertIT.domain.ports;

import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;

public interface WeatherService {
    WeatherData getCurrentWeather(String location);
    GeocodingData getGeocodingData(String address);
}
