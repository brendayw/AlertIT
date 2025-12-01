package com.example.AlertIT.infraestructure.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String from;

    @Value("${twilio.whatsapp.to}")
    private String to;

    @PostConstruct
    public void init() {
        try {
            Twilio.init(accountSid, authToken);

            log.info("Twilio inicializado correctamente");
            log.info("Account SID: {}...", accountSid.substring(0, 10));
            log.info("Número FROM: [{}]", cleanPhoneNumber(from));
            log.info("Número TO: [{}]", cleanPhoneNumber(to));

        } catch (Exception e) {
            log.error("Error al inicializar Twilio", e);
            throw new RuntimeException("No se pudo inicializar Twilio: " + e.getMessage());
        }
    }

    private String cleanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return null;
        return phoneNumber.replaceAll("[\\p{C}\\s]", "").trim();
    }
}
