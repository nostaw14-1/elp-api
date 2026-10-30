package com.elpdeliveries.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class PatientLookupDto {
    private String firstName;
    private String surname;
    private String homeAddress;
    private String homeSuburb;
    private String phoneNumber;
    private String mobileNumber;
    private String source;
    private List<ReceiptItemDto> items;
}
