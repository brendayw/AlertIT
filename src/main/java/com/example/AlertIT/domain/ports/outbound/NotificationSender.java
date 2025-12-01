package com.example.AlertIT.domain.ports.outbound;

public interface NotificationSender {
    void send(String message);
}
