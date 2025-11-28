package com.example.AlertIT.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Alert {
    private final String nivel;
    private final String tipo;
    private final String descripcion;
    private final String recomendacion;
}