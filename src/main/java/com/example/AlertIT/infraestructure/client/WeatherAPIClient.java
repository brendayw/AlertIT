package com.example.AlertIT.infraestructure.client;

import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import com.example.AlertIT.infraestructure.client.dto.WeatherAPIResponse;
import com.example.AlertIT.infraestructure.client.dto.WeatherCurrent;
import com.example.AlertIT.infraestructure.client.dto.WeatherLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeatherAPIClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url:https://api.weatherapi.com/v1}")
    private String apiUrl;

    public WeatherAPIClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherData getCurrentWeather(String location) {
        String url = String.format("%s/current.json?key=%s&q=%s&aqi=no&lang=es",
                apiUrl, apiKey, location);

        log.info("Llamando a WeatherAPI.com: {}", url);

        try {
            WeatherAPIResponse response = restTemplate.getForObject(url, WeatherAPIResponse.class);

            if (response != null) {
                log.debug("Respuesta recibida de WeatherAPI.com para: {}", location);
                return mapToWeatherData(response);
            }

            throw new RuntimeException("No se pudo obtener datos del clima para: " + location);

        } catch (HttpClientErrorException e) {
            log.error("Error al consultar WeatherAPI.com: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error al obtener datos del clima: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al consultar el clima");
        }
    }

    public GeocodingData getGeocodingData(String address) {
        String url = String.format("%s/search.json?key=%s&q=%s",
                apiUrl, apiKey, address);

        log.info("Obteniendo geocoding para: {}", address);

        try {
            WeatherLocation[] locations = restTemplate.getForObject(url, WeatherLocation[].class);

            if (locations == null || locations.length == 0) {
                throw new RuntimeException("No se encontraron coordenadas para: " + address);
            }

            WeatherLocation location = locations[0];
            return mapToGeocodingData(location, address);

        } catch (HttpClientErrorException e) {
            log.error("Error en geocoding: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error al obtener coordenadas: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado en geocoding: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al obtener coordenadas");
        }
    }

    private WeatherData mapToWeatherData(WeatherAPIResponse response) {
        if (response == null || response.getCurrent() == null || response.getLocation() == null) {
            throw new RuntimeException("Respuesta inválida de WeatherAPI");
        }

        WeatherCurrent current = response.getCurrent();
        WeatherLocation location = response.getLocation();

        return new WeatherData(
                current.getTempC(),
                current.getCondition().getText(),
                location.getName(),
                location.getRegion(),
                current.getHumidity(),
                current.getWindKph()
        );
    }

    private GeocodingData mapToGeocodingData(WeatherLocation location, String originalAddress) {
        return new GeocodingData(
                location.getLat(),
                location.getLon(),
                String.format("%s, %s", location.getName(), location.getCountry()),
                location.getName(),
                location.getRegion(),
                location.getCountry()
        );
    }
}