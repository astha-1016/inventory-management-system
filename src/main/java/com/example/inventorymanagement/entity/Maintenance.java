package com.example.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Data
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueDescription;
    private LocalDate maintenanceDate;
    private String technician;
    private Double cost;
    private String status;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;
}