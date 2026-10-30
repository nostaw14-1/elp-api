package com.elpdeliveries.api.repository;

import com.elpdeliveries.api.dto.PatientDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PatientRepository {
    private final JdbcTemplate jdbcTemplate;

    public PatientRepository(@Qualifier("ZJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PatientDto> searchPatients(String firstName, String surname) {
        String sql = """
                SELECT 
                FirstName,
                Surname,
                HomeAddress,
                HomeSuburb,
                PhoneNumber,
                MobileNumber
                FROM Clients
                """;
        ArrayList<Object> params = new ArrayList<>();
        if (firstName != null && !firstName.trim().isEmpty()) {
            sql += " WHERE FirstName LIKE ?";
            params.add("%" + firstName + "%");
        }

        if (surname != null && !surname.trim().isEmpty()) {
            sql += sql.contains("WHERE") ? " AND Surname LIKE ?" : " WHERE Surname LIKE ?";
            params.add("%" + surname + "%");
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PatientDto.class), params.toArray());
    }

    public List<Map<String, Object>> findReceiptRows(
            String receiptId) {
        String sql = """
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

        return jdbcTemplate.queryForList(sql, receiptId);
    }

}
