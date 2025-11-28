package com.example.AlertIT.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeocodingData {
    private Double latitude;
    private Double longitude;
    private String formattedAddress;
    private String city;
    private String country;

    //methods
    public boolean isValidLocation() {
        return latitude != null && longitude != null;
    }
}
