package com.example.AlertIT.infraestructure.adapters.outbound.notification;

import com.example.AlertIT.domain.ports.outbound.NotificationSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioNotificationAdapter implements NotificationSender {

    @Value("${twilio.whatsapp.from}")
    private String from;

    @Value("${twilio.whatsapp.to}")
    private String to;

    public void send(String body) {
        try {
            String cleanFrom = cleanPhoneNumber(from);
            String cleanTo = cleanPhoneNumber(to);

            String fromNumber = cleanFrom.startsWith("whatsapp:") ? cleanFrom : "whatsapp:" + cleanFrom;
            String toNumber = cleanTo.startsWith("whatsapp:") ? cleanTo : "whatsapp:" + cleanTo;

            log.info("Enviando WhatsApp...");
            log.debug("FROM: [{}]", fromNumber);
            log.debug("TO: [{}]", toNumber);

            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    body
            ).create();

            log.info("WhatsApp enviado exitosamente!");
            log.info("SID: {}", message.getSid());
            log.info("Estado: {}", message.getStatus());

        } catch (Exception e) {
            log.error("Error enviando WhatsApp: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar la notificación: " + e.getMessage());
        }
    }

    private String cleanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.replaceAll("[\\p{C}\\s]", "").trim();
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
