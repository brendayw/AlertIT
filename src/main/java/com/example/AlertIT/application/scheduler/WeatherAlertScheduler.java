package com.example.AlertIT.application.scheduler;

import com.example.AlertIT.application.services.TwilioNotificationService;
import com.example.AlertIT.infraestructure.client.WeatherAPIClient;
import com.example.AlertIT.infraestructure.client.dto.WeatherAPIResponse;
import com.example.AlertIT.infraestructure.client.dto.WeatherAlertItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherAlertScheduler {

    private final WeatherAPIClient weatherAPIClient;
    private final TwilioNotificationService twilioNotificationService;

    @Value("${weather.monitor.location:Bahía Blanca}")
    private String monitorLocation;

    //Cada 6 horas
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void checkAlerts() {
        try {
            WeatherAPIResponse weather = weatherAPIClient.getWeatherWithAlerts(monitorLocation);

            if (weatherHasAlert(weather)) {
                String message = getAlertMessage(weather);
                log.warn("¡ALERTA METEOROLÓGICA DETECTADA! {}", message);
                twilioNotificationService.send("ALERTA METEOROLÓGICA: " + message);
            } else {
                log.info("No hay alertas meteorológicas activas para {}", monitorLocation);
            }
        } catch (Exception e) {
            log.error("Error al verificar alertas meteorológicas", e);
        }
    }

    private boolean weatherHasAlert(WeatherAPIResponse weather) {
        return weather != null &&
                weather.getAlerts() != null &&
                weather.getAlerts().getAlert() != null &&
                !weather.getAlerts().getAlert().isEmpty();
    }

    private String getAlertMessage(WeatherAPIResponse weather) {
        if (weatherHasAlert(weather)) {
            WeatherAlertItem firstAlert = weather.getAlerts().getAlert().get(0);
            return String.format("ALERTA METEOROLÓGICA DETECTADA:\n[%s] %s\n%s",
                    firstAlert.getSeverity(),
                    firstAlert.getEffective(),
                    firstAlert.getDesc());
        }
        return "No hay alertas activas";
    }
}
