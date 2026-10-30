package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.dto.PatientDto;
import com.elpdeliveries.api.dto.PatientLookupDto;
import com.elpdeliveries.api.service.PatientService;
import com.elpdeliveries.api.service.ZPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientsController {
    private final ZPatientService service;
    private final PatientService patientService;

    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam(required = false) String firstName,
                                                           @RequestParam(required = false) String surname) {
        List<PatientDto> patients = service.searchPatients(firstName, surname);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/receipt/{receiptId}")
    public ResponseEntity<PatientLookupDto> getReceipt(@PathVariable String receiptId) {
        PatientLookupDto receipt = patientService.getReceipt(receiptId);
        if (receipt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receipt);
    }

}
