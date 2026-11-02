package com.elpdeliveries.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDate createdDate;
    private LocalDate dlvDate;
    private String fname;
    private String lname;
    private String phnum;
    private String homePhone;
    private String addr;
    private String notes;
    private Integer receiptId;
    private String photo;
    private Boolean completed;
    private String completedBy;
    private LocalDate completedDate;
    private Boolean handedToCustomer;
    private Double latitude;
    private Double longitude;
    private Integer routeOrder;
    private Boolean includedInRoute;

    @PrePersist
    private void prePersist() {
        createdDate = LocalDate.now();
    }
}
