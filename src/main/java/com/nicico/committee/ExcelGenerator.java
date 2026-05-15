package com.nicico.committee;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelGenerator {

    static class BaseInfo {
        private String id;
        private TypeInfo type;
        private String persian;
        private String latin;
        private String code;
        private boolean active;
        private BaseInfo parent;
        private Boolean hasChild;
        private int childCount;
        private boolean isDefault;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public TypeInfo getType() { return type; }
        public void setType(TypeInfo type) { this.type = type; }

        public String getPersian() { return persian; }
        public void setPersian(String persian) { this.persian = persian; }

        public String getLatin() { return latin; }
        public void setLatin(String latin) { this.latin = latin; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }

        public BaseInfo getParent() { return parent; }
        public void setParent(BaseInfo parent) { this.parent = parent; }

        public Boolean getHasChild() { return hasChild; }
        public void setHasChild(Boolean hasChild) { this.hasChild = hasChild; }

        public int getChildCount() { return childCount; }
        public void setChildCount(int childCount) { this.childCount = childCount; }

        public boolean isIsDefault() { return isDefault; }
        public void setIsDefault(boolean isDefault) { this.isDefault = isDefault; }

        // Helper method to get parent ID
        public String getParentId() {
            return parent != null ? parent.getId() : null;
        }
    }

    static class TypeInfo {
        private String baseInfoType;
        private String code;

        public String getBaseInfoType() { return baseInfoType; }
        public void setBaseInfoType(String baseInfoType) { this.baseInfoType = baseInfoType; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        @Override
        public String toString() {
            return baseInfoType != null ? baseInfoType : code;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Reading base-infos.json file...");

            // Read JSON file (supports both single object and array)
            List<BaseInfo> all = readJsonFile("/home/seyyed/base.json");
            System.out.println("Loaded " + all.size() + " records from JSON");

            // Print loaded data for debugging
            for (BaseInfo item : all) {
                System.out.println("ID: " + item.getId() +
                        ", Persian: " + item.getPersian() +
                        ", Parent ID: " + item.getParentId());
            }

            // Generate Excel
            ByteArrayInputStream excelStream = generateExcel(all);

            // Save to file
            File outputFile = new File("BaseInfo_output.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = excelStream.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                }
            }

            excelStream.close();
            System.out.println("✅ Excel file generated successfully: " + outputFile.getAbsolutePath());
            System.out.println("File size: " + outputFile.length() + " bytes");

        } catch (Exception e) {
            System.err.println("❌ Error generating Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<BaseInfo> readJsonFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(filePath);

        if (!jsonFile.exists()) {
            throw new FileNotFoundException("JSON file not found: " + filePath);
        }

        // Try to read as array first, if fails try as single object
        try {
            return mapper.readValue(jsonFile,
                    new TypeReference<List<BaseInfo>>() {});
        } catch (Exception e) {
            // If not an array, try reading as single object
            BaseInfo singleItem = mapper.readValue(jsonFile, BaseInfo.class);
            List<BaseInfo> list = new ArrayList<>();
            list.add(singleItem);
            return list;
        }
    }

    private static ByteArrayInputStream generateExcel(List<BaseInfo> all) throws IOException {
        // Build tree map: parentId -> children
        Map<String, List<BaseInfo>> tree = new HashMap<>();
        Map<String, BaseInfo> nodeMap = new HashMap<>();

        // First, map all nodes by ID
        for (BaseInfo item : all) {
            nodeMap.put(item.getId(), item);
        }

        // Build tree structure
        for (BaseInfo item : all) {
            String parentId = item.getParentId();
            tree.computeIfAbsent(parentId, k -> new ArrayList<>()).add(item);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BaseInfo");

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Header row
        Row header = sheet.createRow(0);
        String[] headers = {"Persian", "Latin", "Type", "Code", "Active", "Default", "Has Child", "Child Count"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        AtomicInteger rowIdx = new AtomicInteger(1);

        // Get root nodes (parent is null)
        List<BaseInfo> roots = tree.get(null);

        if (roots != null && !roots.isEmpty()) {
            for (BaseInfo root : roots) {
                writeNode(sheet, root, tree, 0, rowIdx);
            }
        } else {
            System.out.println("No root nodes found (parent=null)");
            // If no roots, write all nodes at root level
            for (BaseInfo node : all) {
                writeFlatNode(sheet, node, rowIdx);
            }
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Set minimum width to 15 characters
            if (sheet.getColumnWidth(i) < 15 * 256) {
                sheet.setColumnWidth(i, 15 * 256);
            }
        }

        // Write to output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void writeNode(Sheet sheet,
                                  BaseInfo node,
                                  Map<String, List<BaseInfo>> tree,
                                  int level,
                                  AtomicInteger rowIdx) {

        // Create row
        Row row = sheet.createRow(rowIdx.getAndIncrement());

        // Create cell style for data rows
        CellStyle dataStyle = sheet.getWorkbook().createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Indentation for Persian column (visual hierarchy)
        String indent = "  ".repeat(Math.max(0, level));
        String persianValue = indent + (node.getPersian() != null ? node.getPersian() : "");

        // Get type string
        String typeStr = "";
        if (node.getType() != null) {
            typeStr = node.getType().toString();
        }

        // Create cells
        createCell(row, 0, persianValue, dataStyle);
        createCell(row, 1, node.getLatin() != null ? node.getLatin() : "", dataStyle);
        createCell(row, 2, typeStr, dataStyle);
        createCell(row, 3, node.getCode() != null ? node.getCode() : "", dataStyle);
        createCell(row, 4, String.valueOf(node.isActive()), dataStyle);
        createCell(row, 5, String.valueOf(node.isIsDefault()), dataStyle);
        createCell(row, 6, node.getHasChild() != null ? String.valueOf(node.getHasChild()) : "", dataStyle);
        createCell(row, 7, String.valueOf(node.getChildCount()), dataStyle);

        // Process children
        List<BaseInfo> children = tree.get(node.getId());

        if (children != null && !children.isEmpty()) {
            int startRow = rowIdx.get();

            // Write all children
            for (BaseInfo child : children) {
                writeNode(sheet, child, tree, level + 1, rowIdx);
            }

            int endRow = rowIdx.get() - 1;

            // Group rows for collapsible hierarchy
            if (startRow <= endRow) {
                sheet.groupRow(startRow, endRow);
                // Collapse by default
                sheet.setRowGroupCollapsed(startRow, true);
            }
        }
    }

    private static void writeFlatNode(Sheet sheet, BaseInfo node, AtomicInteger rowIdx) {
        Row row = sheet.createRow(rowIdx.getAndIncrement());

        CellStyle dataStyle = sheet.getWorkbook().createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        String typeStr = node.getType() != null ? node.getType().toString() : "";

        createCell(row, 0, node.getPersian() != null ? node.getPersian() : "", dataStyle);
        createCell(row, 1, node.getLatin() != null ? node.getLatin() : "", dataStyle);
        createCell(row, 2, typeStr, dataStyle);
        createCell(row, 3, node.getCode() != null ? node.getCode() : "", dataStyle);
        createCell(row, 4, String.valueOf(node.isActive()), dataStyle);
        createCell(row, 5, String.valueOf(node.isIsDefault()), dataStyle);
        createCell(row, 6, node.getHasChild() != null ? String.valueOf(node.getHasChild()) : "", dataStyle);
        createCell(row, 7, String.valueOf(node.getChildCount()), dataStyle);
    }

    private static void createCell(Row row, int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}