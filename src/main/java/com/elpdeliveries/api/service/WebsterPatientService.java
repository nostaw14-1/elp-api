package com.elpdeliveries.api.service;

import com.elpdeliveries.api.dto.PatientLookupDto;
import com.elpdeliveries.api.entity.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebsterPatientService {
    private final SettingsService settingsService;

    private Connection createConnection() throws SQLException {
        Settings settings = settingsService.getSettings();
        String url = "jdbc:sqlserver://"+ settings.getSqlServer() +":50526;databaseName=WEBSTERCARE;encrypt=true;trustServerCertificate=true;";
        return DriverManager.getConnection(
                url,
                settings.getSqlUsername(),
                settings.getSqlPassword()
        );
    }

    public PatientLookupDto getReceipt(String barcode) throws SQLException{
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
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, barcode);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapPatient(rs);
            }
        }
    }

    private PatientLookupDto mapPatient(ResultSet rs) throws SQLException {
        PatientLookupDto dto = new PatientLookupDto();
        dto.setFirstName(rs.getString("patfirstname"));
        dto.setSurname(rs.getString("patsurname"));
        dto.setHomeAddress(rs.getString("pataddress1"));
        dto.setHomeSuburb(rs.getString("pataddress2"));
        dto.setPhoneNumber(rs.getString("phone"));
        dto.setSource("Webstercare");
        dto.setItems(List.of());
        return dto;
    }
}
