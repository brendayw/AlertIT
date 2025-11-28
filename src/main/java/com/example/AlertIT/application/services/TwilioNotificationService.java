package com.example.AlertIT.application.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioNotificationService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}") // "whatsapp:+14155238886"
    private String from;

    @Value("${twilio.whatsapp.to}")   // tu número destino
    private String to;

    @PostConstruct
    public void init() {
        try {
            Twilio.init(accountSid, authToken);
            log.info("Twilio inicializado correctamente");
        } catch (Exception e) {
            log.error("Error al inicializar Twilio", e);
            throw new RuntimeException("No se pudo inicializar Twilio: " + e.getMessage());
        }
    }

    public void send(String body) {
        try {
            log.info("Enviando notificación de WhatsApp a: {}", to);

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + to),
                    new PhoneNumber(from),
                    body
            ).create();

            log.info("WhatsApp enviado exitosamente. SID: {}", message.getSid());
            log.debug("Estado del mensaje: {}", message.getStatus());

        } catch (Exception e) {
            log.error("Error al enviar WhatsApp: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar la notificación: " + e.getMessage());
        }

        // Método alternativo si quieres enviar a múltiples números
//        public void sendToNumber(String phoneNumber, String body) {
//            try {
//                log.info("Enviando notificación de WhatsApp a: {}", phoneNumber);
//
//                Message message = Message.creator(
//                        new PhoneNumber("whatsapp:" + phoneNumber),
//                        new PhoneNumber(from),
//                        body
//                ).create();
//
//                log.info("WhatsApp enviado exitosamente. SID: {}", message.getSid());
//
//            } catch (Exception e) {
//                log.error("Error al enviar WhatsApp a {}: {}", phoneNumber, e.getMessage(), e);
//                throw new RuntimeException("No se pudo enviar la notificación: " + e.getMessage());
//            }
//        }
    }
}
