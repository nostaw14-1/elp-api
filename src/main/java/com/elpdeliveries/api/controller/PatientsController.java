package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.dto.PatientDto;
import com.elpdeliveries.api.dto.PatientLookupDto;
import com.elpdeliveries.api.dto.StaffDto;
import com.elpdeliveries.api.service.PatientService;
import com.elpdeliveries.api.service.ZService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientsController {
    private final ZService service;
    private final PatientService patientService;

    @GetMapping("/patients/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam(required = false) String firstName,
                                                           @RequestParam(required = false) String surname)
                                            throws SQLException {
        List<PatientDto> patients = service.searchPatients(firstName, surname);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patients/receipt/{receiptId}")
    public ResponseEntity<PatientLookupDto> getReceipt(@PathVariable String receiptId) throws Exception {
        PatientLookupDto receipt = patientService.getReceipt(receiptId);
        if (receipt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/staff")
    public List<StaffDto> getStaff() throws SQLException {
        return service.getStaff();
    }

}
