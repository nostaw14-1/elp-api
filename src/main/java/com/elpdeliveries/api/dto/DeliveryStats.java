package com.elpdeliveries.api.dto;

public record DeliveryStats(
        long todayCount,
        long uncompletedCount
) {}
