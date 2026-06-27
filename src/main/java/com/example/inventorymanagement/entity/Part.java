package com.example.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parts")
@Data
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partName;      // RAM, Toner, Keyboard etc.
    private String category;      // Computer Part / Printer Part / Hardware
    private Integer quantity;
    private Integer minimumStock;
    private Double unitPrice;
    private String supplier;
}