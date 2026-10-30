package com.elpdeliveries.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PatientDto {
    private String firstName;
    private String surname;
    private String homeAddress;
    private String homeSuburb;
    private String phoneNumber;
    private String mobileNumber;
}
