package com.example.AlertIT.presentation.dto;

import com.example.AlertIT.domain.enums.AlertLevel;
import com.example.AlertIT.domain.enums.AlertType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AlertItemResponse(
        @JsonProperty("nivel") AlertLevel nivel,
        @JsonProperty("tipo") AlertType tipo,
        @JsonProperty("descripcion") String descripcion,
        @JsonProperty("recomendacion") String recomendacion
) {
}
