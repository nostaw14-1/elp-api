package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientLookupDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsterPatientService {
    private final JdbcTemplate webstercareJdbcTemplate;

    public WebsterPatientService(@Qualifier("WebstercareJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.webstercareJdbcTemplate = jdbcTemplate;
    }

    public PatientLookupDto getReceipt(String barcode) {
        String sql = """
                SELECT
                    pd.barcode_128,
                    pt.patfirstname,
                    pt.patsurname,
                    pt.pataddress1,
                    pt.pataddress2,
                    pt.phone
                FROM PrintDocument pd
                         INNER JOIN Metafoil m
                                    ON pd.metafoilcode = m.lMetaFoilCode
                         INNER JOIN PatientTable pt
                                    ON m.patientid = pt.patientid
                WHERE pd.barcode_128 = ?;
                """;
        List<PatientLookupDto> matches = webstercareJdbcTemplate.query(sql, (rs, rowNum) -> {
            PatientLookupDto dto = new PatientLookupDto();
            dto.setFirstName(rs.getString("patfirstname"));
            dto.setSurname(rs.getString("patsurname"));
            dto.setHomeAddress(rs.getString("pataddress1"));
            dto.setHomeSuburb(rs.getString("pataddress2"));
            dto.setPhoneNumber(rs.getString("phone"));
            dto.setSource("Webstercare");
            dto.setItems(List.of());
            return dto;
        }, barcode);
        return matches.isEmpty() ? null : matches.get(0);
    }
}
