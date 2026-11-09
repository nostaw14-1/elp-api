package com.elpdeliveries.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RouteDto {
    private Long id;
    private String staffName;
    private LocalDateTime createdAt;
    private String routeStatus;
    private Integer stopCount;
}
