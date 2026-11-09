package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientLookupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final ZService zService;
    private final WebsterPatientService websterPatientService;

    public PatientLookupDto getReceipt(String receiptId) throws Exception {
        PatientLookupDto patient = zService.getReceipt(receiptId);

        if (patient != null) {
            return patient;
        }

        return websterPatientService.getReceipt(receiptId);

    }
}
