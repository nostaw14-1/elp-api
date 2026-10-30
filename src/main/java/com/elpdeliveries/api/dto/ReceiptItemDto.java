package com.elpdeliveries.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptItemDto {
    private String description;
    private Integer qty;
    private BigDecimal sellPrice;
}
