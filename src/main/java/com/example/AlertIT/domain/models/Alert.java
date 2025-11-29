package com.example.AlertIT.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Alert {
    private AlertLevel nivel;
    private AlertType tipo;
    private final String descripcion;
    private final String recomendacion;
}