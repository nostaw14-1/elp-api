package com.elpdeliveries.api.entity;

import java.math.BigDecimal;

public record ReceiptRow(
        String firstName,
        String surname,
        String homeAddress,
        String homeSuburb,
        String phoneNumber,
        String mobileNumber,
        String description,
        Integer qty,
        BigDecimal sellPrice
) {
}
