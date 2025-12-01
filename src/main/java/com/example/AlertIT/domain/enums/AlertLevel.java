package com.example.AlertIT.domain.enums;

public enum AlertLevel {
    VERDE(0),       // Sin alerta
    AMARILLO(1),
    NARANJA(2),
    ROJO(3);

    private final int severityValue;

    AlertLevel(int v) {
        this.severityValue = v;
    }

    public int getSeverityValue() {
        return severityValue;
    }
}
