package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientDto;
import com.elpdeliveries.api.dto.PatientLookupDto;
import com.elpdeliveries.api.dto.ReceiptItemDto;
import com.elpdeliveries.api.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ZPatientService {
    private final PatientRepository repository;
    public ZPatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public PatientLookupDto getReceipt(String receiptId) {
        List<Map<String, Object>> rows = repository.findReceiptRows(receiptId);

        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> first = rows.get(0);

        PatientLookupDto response = new PatientLookupDto();
        response.setFirstName((String) first.get("FirstName"));
        response.setSurname((String) first.get("Surname"));
        response.setHomeAddress((String) first.get("HomeAddress"));
        response.setHomeSuburb((String) first.get("HomeSuburb"));
        response.setPhoneNumber((String) first.get("PhoneNumber"));
        response.setMobileNumber((String) first.get("MobileNumber"));
        response.setSource("Z");

        List<ReceiptItemDto> items = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            ReceiptItemDto item = new ReceiptItemDto();
            item.setDescription((String) row.get("Description"));
            item.setQty(((Number) row.get("Qty")).intValue());
            item.setSellPrice((BigDecimal) row.get("SellPrice"));
            items.add(item);
        }

        response.setItems(items);
        return response;
    }

    public List<PatientDto> searchPatients(String firstName, String surname) {
        if (firstName == null && surname == null) {
            throw new RuntimeException("Please input search params");
        }
        return repository.searchPatients(firstName, surname);
    }
}
