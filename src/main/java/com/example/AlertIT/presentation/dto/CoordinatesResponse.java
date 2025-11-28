package com.example.AlertIT.presentation.dto;

import com.example.AlertIT.domain.models.Coordinates;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordinatesResponse(
        @JsonProperty("latitude") double latitude,
        @JsonProperty("longitude") double longitude
) {
    public static CoordinatesResponse fromDomain(Coordinates coordinates) {
        return new CoordinatesResponse(
                coordinates.getLatitude(),
                coordinates.getLongitude()
        );
    }
}
