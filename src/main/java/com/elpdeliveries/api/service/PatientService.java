package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientLookupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final ZPatientService zPatientService;
    private final WebsterPatientService websterPatientService;

    public PatientLookupDto getReceipt(String receiptId) {
        PatientLookupDto patient = zPatientService.getReceipt(receiptId);

        if (patient != null) {
            return patient;
        }

        return websterPatientService.getReceipt(receiptId);

    }
}
