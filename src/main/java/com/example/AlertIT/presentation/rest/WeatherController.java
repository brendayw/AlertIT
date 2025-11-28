package com.example.AlertIT.presentation.rest;

import com.example.AlertIT.application.services.AlertService;
import com.example.AlertIT.domain.models.GeocodingData;
import com.example.AlertIT.domain.models.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final AlertService alertService;

    @GetMapping("/current")
    public ResponseEntity<WeatherData> getCurrentWeather(@RequestParam String location) {
        try {
            WeatherData weather = alertService.getWeatherForLocation(location);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/geocoding")
    public ResponseEntity<GeocodingData> getGeocoding(@RequestParam String address) {
        try {
            GeocodingData geocoding = alertService.getCoordinatesForAddress(address);
            return ResponseEntity.ok(geocoding);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/alert")
    public ResponseEntity<Map<String, Object>> processAlert(@RequestParam String address) {
        try {
            Map<String, Object> alertResult = alertService.processLocationAlert(address);
            return ResponseEntity.ok(alertResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "nivelAlerta", "ERROR"
            ));
        }
    }
}