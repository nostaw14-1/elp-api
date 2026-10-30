package com.elpdeliveries.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {
    private List<Integer> deliveryIds;
}
