package com.example.AlertIT.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinates {
    private double latitude;
    private double longitude;

    //methods
    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }
}
