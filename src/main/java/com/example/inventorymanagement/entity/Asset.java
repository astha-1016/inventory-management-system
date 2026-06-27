package com.example.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
@Data
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetName;       // Desktop, Printer, UPS etc.
    private String assetType;       // Computer / Printer / Hardware
    private String brand;
    private String model;
    private String serialNumber;

    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;

    private String status;          // Available / In Use / Under Repair
    private String location;
}