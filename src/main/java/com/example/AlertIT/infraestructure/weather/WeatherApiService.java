package com.example.AlertIT.infraestructure.weather;

import com.example.AlertIT.domain.models.GeocodingData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public WeatherApiService(RestTemplate restTemplate,
                             @Value("${weather.api.url}") String apiUrl,
                             @Value("${weather.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public String getWeatherData(String location) {
        String url = String.format("%s/current.json?key=%s&q=%s",
                apiUrl, apiKey, location);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public GeocodingData getGeocodingData(String address) {
        String url = String.format("%s/search.json?key=%s&q=%s",
                apiUrl, apiKey, address);

        ResponseEntity<GeocodingData> response = restTemplate.getForEntity(url, GeocodingData.class);
        return response.getBody();
    }
}
