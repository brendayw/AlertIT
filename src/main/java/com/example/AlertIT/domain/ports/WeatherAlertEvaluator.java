package com.example.AlertIT.domain.ports;

import com.example.AlertIT.domain.models.Alert;
import com.example.AlertIT.domain.models.WeatherData;

import java.util.List;

public interface WeatherAlertEvaluator {
    List<Alert> evaluate(WeatherData weather);
}
