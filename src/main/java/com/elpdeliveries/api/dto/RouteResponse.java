package com.elpdeliveries.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RouteResponse {
    private Long routeId;
    private String driverName;
    private Integer deliveryCount;
    private LocalDateTime createdAt;
}
