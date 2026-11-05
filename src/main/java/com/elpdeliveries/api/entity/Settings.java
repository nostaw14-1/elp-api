package com.elpdeliveries.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "settings")
@Getter
@Setter
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String vroomIp;
    private Double depotLat;
    private Double depotLong;
    private String awsBucket;
    private String awsAccessKey;
    private String awsSecretKey;
    private String sqlServer;
    private String sqlUsername;
    private String sqlPassword;
}
