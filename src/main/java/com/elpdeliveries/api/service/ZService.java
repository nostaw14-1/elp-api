package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientDto;
import com.elpdeliveries.api.dto.PatientLookupDto;
import com.elpdeliveries.api.dto.ReceiptItemDto;
import com.elpdeliveries.api.dto.StaffDto;
import com.elpdeliveries.api.entity.ReceiptRow;
import com.elpdeliveries.api.repository.ZRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZService {
    private final ZRepository repository;


    public PatientLookupDto getReceipt(String receiptId) throws SQLException {
        List<ReceiptRow> rows = repository.findReceiptRows(receiptId);

        if (rows.isEmpty()) {
            return null;
        }

        ReceiptRow first = rows.get(0);

        PatientLookupDto response = new PatientLookupDto();
        response.setFirstName(first.firstName());
        response.setSurname(first.surname());
        response.setHomeAddress(first.homeAddress());
        response.setHomeSuburb(first.homeSuburb());
        response.setPhoneNumber(first.phoneNumber());
        response.setMobileNumber(first.mobileNumber());
        response.setSource("Z");

        List<ReceiptItemDto> items = new ArrayList<>();

        for (ReceiptRow row : rows) {
            ReceiptItemDto item = new ReceiptItemDto();
            item.setDescription(row.description());
            item.setQty(row.qty());
            item.setSellPrice(row.sellPrice());
            items.add(item);
        }
        response.setItems(items);
        return response;
    }

    public List<PatientDto> searchPatients(String firstName, String surname) throws SQLException {
        if (firstName == null && surname == null) {
            throw new RuntimeException("Please input search params");
        }
        return repository.searchPatients(firstName, surname);
    }

    public List<StaffDto> getStaff() throws SQLException {
        return repository.getAllStaff();
    }
}
