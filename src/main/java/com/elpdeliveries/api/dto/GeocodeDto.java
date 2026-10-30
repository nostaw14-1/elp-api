package com.elpdeliveries.api.dto;

import lombok.Data;

@Data
public class GeocodeDto {
    private Double latitude;
    private Double longitude;

    public GeocodeDto(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
