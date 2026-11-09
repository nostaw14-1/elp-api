package com.elpdeliveries.api.repository;

import com.elpdeliveries.api.dto.PatientDto;
import com.elpdeliveries.api.dto.StaffDto;
import com.elpdeliveries.api.entity.ReceiptRow;
import com.elpdeliveries.api.entity.Settings;
import com.elpdeliveries.api.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ZRepository {
    private final SettingsService settingsService;
    private Connection createConnection() throws SQLException {
        Settings settings = settingsService.getSettings();
        String url = "jdbc:sqlserver://"+ settings.getSqlServer() +";databaseName=APSS;encrypt=true;trustServerCertificate=true;";
        return DriverManager.getConnection(
                url,
                settings.getSqlUsername(),
                settings.getSqlPassword()
        );
    }

    public List<PatientDto> searchPatients(String firstName, String surname) throws SQLException {
        List<PatientDto> patients = new ArrayList<>();
        ArrayList<Object> params = new ArrayList<>();
        String SQL = """
                SELECT
                FirstName,
                Surname,
                HomeAddress,
                HomeSuburb,
                PhoneNumber,
                MobileNumber
                FROM Clients
                """;

        if (firstName != null && !firstName.trim().isEmpty()) {
            SQL += " WHERE FirstName LIKE ?";
            params.add("%" + firstName + "%");
        }

        if (surname != null && !surname.trim().isEmpty()) {
            SQL += SQL.contains("WHERE") ? " AND Surname LIKE ?" : " WHERE Surname LIKE ?";
            params.add("%" + surname + "%");
        }

        try (Connection conn = createConnection();
            PreparedStatement ps = conn.prepareStatement(SQL)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PatientDto patient = new PatientDto();
                    patient.setFirstName(rs.getString("FirstName"));
                    patient.setFirstName(rs.getString("FirstName"));
                    patient.setSurname(rs.getString("Surname"));
                    patient.setHomeAddress(rs.getString("HomeAddress"));
                    patient.setHomeSuburb(rs.getString("HomeSuburb"));
                    patient.setPhoneNumber(rs.getString("PhoneNumber"));
                    patient.setMobileNumber(rs.getString("MobileNumber"));
                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    public List<ReceiptRow> findReceiptRows(String receiptId) throws SQLException {
        String SQL = """
                SELECT
                    c.FirstName,
                    c.Surname,
                    c.HomeAddress,
                    c.HomeSuburb,
                    c.PhoneNumber,
                    c.MobileNumber,
                    ri.Description,
                    ri.Qty,
                    ri.SellPrice
                FROM Receipts r
                INNER JOIN Clients c
                    ON r.clientID = c.ID
                INNER JOIN ReceiptItems ri
                    ON r.ID = ri.receiptID
                WHERE r.ID = ?
                """;
        List<ReceiptRow> rows = new ArrayList<>();
        try (Connection conn = createConnection();
            PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, receiptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(
                            new ReceiptRow(
                                    rs.getString("FirstName"),
                                    rs.getString("Surname"),
                                    rs.getString("HomeAddress"),
                                    rs.getString("HomeSuburb"),
                                    rs.getString("PhoneNumber"),
                                    rs.getString("MobileNumber"),
                                    rs.getString("Description"),
                                    rs.getInt("Qty"),
                                    rs.getBigDecimal("SellPrice")
                            )
                    );
                }
            }
        }
        return rows;
    }

    public List<StaffDto> getAllStaff() throws SQLException {
        String sql = """
                SELECT ID, FirstName, Surname
                FROM Staff WHERE Status = 'A'
                ORDER BY Created DESC
                """;
        List<StaffDto> result = new ArrayList<>();
        try (Connection conn = createConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(
                        new StaffDto(
                                rs.getString("ID"),
                                rs.getString("FirstName"),
                                rs.getString("Surname")
                        )
                );
            }
        }
        return result;
    }

    public StaffDto getStaffById(String staffId) throws SQLException {
        String sql = """
                SELECT ID, FirstName, Surname
                FROM Staff WHERE ID = ?
                ORDER BY Created DESC
                """;
        StaffDto result = null;
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result = new StaffDto(
                        rs.getString("ID"),
                        rs.getString("FirstName"),
                        rs.getString("Surname")
                );
            }
        }
        return result;
    }

}
