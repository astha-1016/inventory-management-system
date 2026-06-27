package com.astha.inventory_management.controller;

import com.astha.inventory_management.service.ExcelExportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ExcelExportService excelExportService;

    public ReportController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportInventoryReport() throws IOException {

        byte[] excelFile = excelExportService.exportInventoryReport();

        String filename = "Inventory_Report_" + LocalDate.now() + ".xlsx";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(filename)
                        .build());

        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }
}