package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Asset;
import com.astha.inventory_management.entity.Maintenance;
import com.astha.inventory_management.entity.Part;
import com.astha.inventory_management.repository.AssetRepository;
import com.astha.inventory_management.repository.MaintenanceRepository;
import com.astha.inventory_management.repository.PartRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    private final AssetRepository assetRepository;
    private final PartRepository partRepository;
    private final MaintenanceRepository maintenanceRepository;

    public ExcelExportService(AssetRepository assetRepository,
                              PartRepository partRepository,
                              MaintenanceRepository maintenanceRepository) {

        this.assetRepository = assetRepository;
        this.partRepository = partRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    public byte[] exportInventoryReport() throws IOException {

        Workbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = createHeaderStyle(workbook);

        createAssetsSheet(workbook, headerStyle);

        createPartsSheet(workbook, headerStyle);

        createLowStockSheet(workbook, headerStyle);

        createMaintenanceSheet(workbook, headerStyle);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        workbook.write(outputStream);

        workbook.close();

        return outputStream.toByteArray();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle headerStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return headerStyle;
    }

    private void createAssetsSheet(Workbook workbook, CellStyle headerStyle) {

        Sheet sheet = workbook.createSheet("Assets");

        String[] columns = {
                "ID",
                "Asset Name",
                "Asset Type",
                "Brand",
                "Model",
                "Serial Number",
                "Purchase Date",
                "Warranty Expiry",
                "Status",
                "Location"
        };

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Asset> assets = assetRepository.findAll();

        int rowNum = 1;

        for (Asset asset : assets) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(asset.getId());
            row.createCell(1).setCellValue(asset.getAssetName());
            row.createCell(2).setCellValue(asset.getAssetType());
            row.createCell(3).setCellValue(asset.getBrand());
            row.createCell(4).setCellValue(asset.getModel());
            row.createCell(5).setCellValue(asset.getSerialNumber());

            row.createCell(6).setCellValue(
                    asset.getPurchaseDate() != null
                            ? asset.getPurchaseDate().toString()
                            : "");

            row.createCell(7).setCellValue(
                    asset.getWarrantyExpiry() != null
                            ? asset.getWarrantyExpiry().toString()
                            : "");

            row.createCell(8).setCellValue(asset.getStatus());
            row.createCell(9).setCellValue(asset.getLocation());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createPartsSheet(Workbook workbook, CellStyle headerStyle) {

        Sheet sheet = workbook.createSheet("Parts");

        String[] columns = {
                "ID",
                "Part Name",
                "Category",
                "Quantity",
                "Minimum Stock",
                "Supplier",
                "Unit Price"
        };

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);
        }

        List<Part> parts = partRepository.findAll();

        int rowNum = 1;

        for (Part part : parts) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(part.getId());
            row.createCell(1).setCellValue(part.getPartName());
            row.createCell(2).setCellValue(part.getCategory());
            row.createCell(3).setCellValue(part.getQuantity());
            row.createCell(4).setCellValue(part.getMinimumStock());
            row.createCell(5).setCellValue(part.getSupplier());
            row.createCell(6).setCellValue(part.getUnitPrice());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createLowStockSheet(Workbook workbook, CellStyle headerStyle) {

        Sheet sheet = workbook.createSheet("Low Stock");

        String[] columns = {
                "ID",
                "Part Name",
                "Category",
                "Quantity",
                "Minimum Stock",
                "Supplier",
                "Unit Price"
        };

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);

        }

        List<Part> lowStockParts = partRepository.findLowStockParts();

        int rowNum = 1;

        for (Part part : lowStockParts) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(part.getId());
            row.createCell(1).setCellValue(part.getPartName());
            row.createCell(2).setCellValue(part.getCategory());
            row.createCell(3).setCellValue(part.getQuantity());
            row.createCell(4).setCellValue(part.getMinimumStock());
            row.createCell(5).setCellValue(part.getSupplier());
            row.createCell(6).setCellValue(part.getUnitPrice());

        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createMaintenanceSheet(Workbook workbook, CellStyle headerStyle) {

        Sheet sheet = workbook.createSheet("Maintenance");

        String[] columns = {
                "ID",
                "Asset",
                "Issue Description",
                "Maintenance Date",
                "Technician",
                "Cost",
                "Status"
        };

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);

        }

        List<Maintenance> maintenanceList = maintenanceRepository.findAll();

        int rowNum = 1;

        for (Maintenance maintenance : maintenanceList) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(maintenance.getId());

            row.createCell(1).setCellValue(
                    maintenance.getAsset() != null
                            ? maintenance.getAsset().getAssetName()
                            : "");

            row.createCell(2).setCellValue(maintenance.getIssueDescription());

            row.createCell(3).setCellValue(
                    maintenance.getMaintenanceDate() != null
                            ? maintenance.getMaintenanceDate().toString()
                            : "");

            row.createCell(4).setCellValue(maintenance.getTechnician());

            row.createCell(5).setCellValue(
                    maintenance.getCost() != null
                            ? maintenance.getCost()
                            : 0);

            row.createCell(6).setCellValue(maintenance.getStatus());

        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}