package com.nicico.companies.util;


import com.nicico.companies.util.base.FieldRq;
import com.nicico.companies.util.base.ReportRq;
import com.nicico.companies.util.base.enumeration.ExportType;
import com.nicico.copper.common.domain.i18n.CaptionFactory;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.core.SecurityUtil;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


@Component
@RequiredArgsConstructor
public class JasperUtil {


    private static final String SHABNAM_FD_FONT = "Shabnam-FD";
    private static final String B_TITR_FONT = "B Titr";
    private static final Color DEFAULT_HEADER_BACKCOLOR = Color.decode("#EFEFEF");
    private static final Double MIN_COLUMN_WIDTH = 25D;
    private static final int DEFAULT_COLUMN_HEIGHT = 30;
    private static final int DEFAULT_COLUMN_HEADER_HEIGHT = 30;
    private static final int DEFAULT_MARGIN = 10;
    private static final Float DEFAULT_FONT_SIZE = 8F;
    private static final Float DEFAULT_HEADER_FONT_SIZE = 10F;
    private static int STYLE_ID = 1;
    private static final String DEFAULT_IMAGE_ADDRESS = "reports/images/logo-gray.png";

    public Resource createReport(ReportRq reportRq, List<Map<String, ?>> data) throws JRException {



            JasperDesign jasperDesign = generateJasperDesign(reportRq, data);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("title", CaptionFactory.getLabel(reportRq.getPropertyRq().getTitle()));
            parameters.put("reportTitle", "");
            parameters.put("creator", SecurityUtil.getFullName());
            parameters.put("date",convertToJalaliStrForPDF(new Timestamp(new Date().getTime())));
            parameters.put("hasHeader", "true");
            parameters.put("hasSubtitle", "");
            parameters.put("hasTitle", "true");
            parameters.put("hasFooter", true);
            parameters.put("number", reportRq.getNumber());
            parameters.put("companyLogo", true);
            parameters.put("subtitle", "");
            parameters.put("filtersStr", "");
            parameters.put("pagination", true);
            parameters.put("companyName",true );
            parameters.put("gridSubTitle",String.format(CaptionFactory.getLabel("report.by") ,SecurityUtil.getFullName(),
                    SecurityUtil.getUsername()));
//            parameters.put("imageAddress", DEFAULT_IMAGE_ADDRESS);
            parameters.put("collection", data);
        if(ExportType.EXCEL.equals(
                reportRq.getPropertyRq() != null ? reportRq.getPropertyRq().getType() : null
        )){
            parameters.put("pagination", false);
        }


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            byte[] reportBytes = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if(ExportType.EXCEL.equals(
                reportRq.getPropertyRq() != null ? reportRq.getPropertyRq().getType() : null
        )){
            jasperPrint.setProperty(
                    JRParameter.IS_IGNORE_PAGINATION, "true"
            );
                exportXlsx(jasperPrint, byteArrayOutputStream);
                reportBytes = byteArrayOutputStream.toByteArray();
            }else
                reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            return new ByteArrayResource(reportBytes);


    }

    private JasperDesign generateJasperDesign(ReportRq reportRq, List<Map<String, ?>> data) throws JRException {
        JasperDesign jasperDesign;
        if (reportRq.getPropertyRq().getOrientation() == OrientationEnum.LANDSCAPE) {
            jasperDesign = JRXmlLoader.load(this.getClass().getClassLoader().getResourceAsStream("reports/templates/template_subreport_landscape.jrxml"));
        } else if(ExportType.EXCEL.equals(
                reportRq.getPropertyRq() != null ? reportRq.getPropertyRq().getType() : null
        )){
            jasperDesign = JRXmlLoader.load(this.getClass().getClassLoader().getResourceAsStream("reports/templates/template_subreport_portrait_excel.jrxml"));
        }
        else{
            jasperDesign = JRXmlLoader.load(this.getClass().getClassLoader().getResourceAsStream("reports/templates/template_subreport_portrait.jrxml"));
        }

        jasperDesign.setPageWidth(reportRq.getPropertyRq().getWidth());
        jasperDesign.setPageHeight(reportRq.getPropertyRq().getHeight());
        jasperDesign.setUUID(UUID.randomUUID());
        jasperDesign.setName("DynamicReportDesign");

        JRSection detailSection = jasperDesign.getDetailSection();
        JRBand[] bands = detailSection.getBands();
        JRDesignComponentElement jrChild = (JRDesignComponentElement) bands[0].getChildren().get(0);
        StandardTable standardTable = (StandardTable) jrChild.getComponent();
        FieldRq[] fields = reportRq.getFields();
        JRDesignDataset dataset = (JRDesignDataset) jasperDesign.getDatasets()[0];

        Arrays.sort(fields, Comparator.comparing(FieldRq::getPriority));

        calculateDynamicWidthForMap(fields, data, (double) reportRq.getPropertyRq().getWidth());

        for (FieldRq field : fields) {
            StandardColumn column = createColumnForMap(field, dataset);
            standardTable.addColumn(column);
            jasperDesign.addStyle(column.getDetailCell().getStyle());
            jasperDesign.addStyle(column.getColumnHeader().getStyle());
        }

        StandardColumn rowNumberColumn = createRowNumberColumn();
        standardTable.addColumn(rowNumberColumn);
        JRStyle rowNumStyle = rowNumberColumn.getDetailCell().getStyle();
        jasperDesign.addStyle(rowNumStyle);

        return jasperDesign;
    }

    private void calculateDynamicWidthForMap(FieldRq[] fields, List<Map<String, ?>> data, Double reportWidth) {
        if (fields.length == 0) return;

        Map<String, Double> weights = new HashMap<>();
        double totalWeight = 0;

        boolean hasUserDefinedWidth = false;
        for (FieldRq field : fields) {
            if (field.getWidth() != null && field.getWidth() > 0) {
                hasUserDefinedWidth = true;
                break;
            }
        }

        if (hasUserDefinedWidth) {
            final double DEFAULT_WEIGHT = 100.0;
            for (FieldRq field : fields) {
                double weight = (field.getWidth() != null && field.getWidth() > 0) ? field.getWidth() : DEFAULT_WEIGHT;
                weights.put(field.getName(), weight);
                totalWeight += weight;
            }
        } else {
            for (FieldRq field : fields) {
                double contentWeight = 0;
                if (data != null && !data.isEmpty()) {
                    for (Map<String, ?> row : data) {
                        Object value = row.get(field.getName());
                        if (value != null) {
                            contentWeight += String.valueOf(value).length();
                        }
                    }
                }
                double headerWeight = (field.getFarsiName() != null ? field.getFarsiName().length() : 0) * 1.5;
                double weight = contentWeight + headerWeight;
                if (weight <= 0) {
                    weight = 50;
                }
                weights.put(field.getName(), weight);
                totalWeight += weight;
            }
        }

        double availableWidth = reportWidth - MIN_COLUMN_WIDTH - (2 * DEFAULT_MARGIN);

        if (totalWeight > 0) {
            for (FieldRq field : fields) {
                double weight = weights.get(field.getName());
                double calculatedWidth = (weight / totalWeight) * availableWidth;
                field.setWidth(calculatedWidth);
            }

            double deficit = 0;
            List<FieldRq> aboveMinFields = new ArrayList<>();
            for (FieldRq field : fields) {
                if (field.getWidth() < MIN_COLUMN_WIDTH) {
                    deficit += MIN_COLUMN_WIDTH - field.getWidth();
                    field.setWidth(MIN_COLUMN_WIDTH);
                } else {
                    aboveMinFields.add(field);
                }
            }

            if (deficit > 0 && !aboveMinFields.isEmpty()) {
                double totalWidthOfAboveMin = 0;
                for (FieldRq field : aboveMinFields) {
                    totalWidthOfAboveMin += field.getWidth();
                }

                for (FieldRq field : aboveMinFields) {
                    double reduction = (field.getWidth() / totalWidthOfAboveMin) * deficit;
                    if (field.getWidth() - reduction >= MIN_COLUMN_WIDTH) {
                        field.setWidth(field.getWidth() - reduction);
                    }
                }
            }

            double currentTotalWidth = 0;
            for (FieldRq field : fields) {
                currentTotalWidth += field.getWidth();
            }
            if (currentTotalWidth > 0) {
                double scale = availableWidth / currentTotalWidth;
                for (FieldRq field : fields) {
                    field.setWidth(field.getWidth() * scale);
                }
            }

        } else {
            double equalShare = availableWidth / fields.length;
            for (FieldRq field : fields) {
                field.setWidth(equalShare);
            }
        }
    }


    private StandardColumn createColumnForMap(FieldRq queryField, JRDesignDataset dataset) throws JRException {
        addFieldToDatasetForMap(queryField, dataset);
        StandardColumn column = createColumn(queryField.getWidth());
        JRStyle headerStyle = createHeaderStyle(queryField);
        JRStyle cellStyle = createCellStyle(queryField);

        column.setColumnHeader(createColumnHeader("$R{" + queryField.getFarsiName() + "}", queryField.getWidth(), headerStyle));
        JRDesignExpression expression = createFieldDesignExpressionForMap(queryField);
        column.setDetailCell(createDetailCell(expression, cellStyle, queryField.getWidth()));
        return column;
    }

    private void addFieldToDatasetForMap(FieldRq field, JRDesignDataset dataset) throws JRException {
        JRDesignField jrDesignField = new JRDesignField();
        jrDesignField.setName(field.getName());
        jrDesignField.setValueClassName("java.lang.Object");
        dataset.addField(jrDesignField);
    }

    private JRDesignExpression createFieldDesignExpressionForMap(FieldRq queryField) {
        JRDesignExpression expression = new JRDesignExpression();
        String fieldName = queryField.getName();
        expression.setText(String.format("$F{%s} == null ? \"-\" : $F{%s}.toString()", fieldName, fieldName));
        expression.setValueClassName("java.lang.String");
        return expression;
    }

    private void exportXlsx(JasperPrint jasperPrint, OutputStream out) throws JRException {
        JRXlsExporter jrXlsExporter = new JRXlsExporter();
        jrXlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        jrXlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setDetectCellType(true);
        configuration.setSheetDirection(RunDirectionEnum.LTR);
        jrXlsExporter.setConfiguration(configuration);
        jrXlsExporter.exportReport();
    }

    private StandardColumn createRowNumberColumn() {
        StandardColumn column = createColumn(MIN_COLUMN_WIDTH);
        JRDesignStyle style = createDefaultStyle();
        column.setColumnHeader(createColumnHeader("$R{report.row_number}", MIN_COLUMN_WIDTH, style));
        column.setDetailCell(createDetailCell(createRowNumberDesignExpression(), style, MIN_COLUMN_WIDTH));
        return column;
    }

    private DesignCell createDetailCell(JRDesignExpression expression, JRStyle style, Double width) {
        DesignCell designCell = new DesignCell();
        designCell.setHeight(DEFAULT_COLUMN_HEIGHT);
        designCell.addElement(createTextField(expression, style, width, false));
        designCell.setStyle(style);
        return designCell;
    }

    private JRDesignExpression createRowNumberDesignExpression() {
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("$V{REPORT_COUNT}");
        expression.setValueClass(String.class);
        return expression;
    }

    private DesignCell createColumnHeader(String expressionText, Double textFieldWidth, JRStyle style) {
        DesignCell columnHeader = new DesignCell();
        columnHeader.setHeight(DEFAULT_COLUMN_HEADER_HEIGHT);
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText(expressionText);
        expression.setValueClass(String.class);
        JRDesignTextField textField = createTextField(expression, style, textFieldWidth, true);
        columnHeader.addElement(textField);
        columnHeader.setStyle(style);
        return columnHeader;
    }

    private JRDesignTextField createTextField(JRDesignExpression expression, JRStyle style, Double width, Boolean header) {
        JRDesignTextField textField = new JRDesignTextField();
        textField.setHeight(DEFAULT_COLUMN_HEIGHT);
        textField.setX(0);
        textField.setY(0);
        textField.setFontName(header ? B_TITR_FONT : SHABNAM_FD_FONT);
        textField.setUUID(UUID.randomUUID());
        textField.setWidth(width.intValue());
        textField.setStyle(style);
        textField.setExpression(expression);
        textField.setStretchWithOverflow(true);
        textField.getLineBox().getPen().setLineColor(Color.BLACK);
        textField.getLineBox().getPen().setLineWidth(0.5F);
        return textField;
    }

    private JRStyle createHeaderStyle(FieldRq queryField) {
        JRDesignStyle style = createDefaultStyle();
        style.setBold(Optional.ofNullable(queryField.getBold()).orElse(true));
        style.setItalic(Optional.ofNullable(queryField.getItalic()).orElse(false));
        style.setFontName(Optional.ofNullable(queryField.getHeaderFontName()).orElse(B_TITR_FONT));
        style.setFontSize(Optional.ofNullable(queryField.getHeaderFontSize()).orElse(DEFAULT_HEADER_FONT_SIZE));
        style.setBackcolor(Optional.ofNullable(queryField.getHeaderBackColor()).map(Color::decode).orElse(DEFAULT_HEADER_BACKCOLOR));
        return style;
    }

    private JRStyle createCellStyle(FieldRq queryField) {
        JRDesignStyle style = createDefaultStyle();
        style.setBold(Optional.ofNullable(queryField.getBold()).orElse(false));
        style.setItalic(Optional.ofNullable(queryField.getItalic()).orElse(false));
        style.setFontName(Optional.ofNullable(queryField.getFontName()).orElse(SHABNAM_FD_FONT));
        style.setFontSize(Optional.ofNullable(queryField.getFontSize()).orElse(DEFAULT_FONT_SIZE));
        if (queryField.getBackColor() != null) {
            style.setBackcolor(Color.decode(queryField.getBackColor()));
        }
        return style;
    }

    private JRDesignStyle createDefaultStyle() {
        JRDesignStyle style = new JRDesignStyle();
        style.setName("STYLE_" + STYLE_ID++);
        style.setFontName(SHABNAM_FD_FONT);
        JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
        JRDesignExpression conditionExpression = new JRDesignExpression();
        conditionExpression.setText("new Boolean($V{REPORT_COUNT}.intValue()%2==0)");
        conditionExpression.setValueClass(Boolean.class);
        conditionalStyle.setConditionExpression(conditionExpression);
        style.addConditionalStyle(conditionalStyle);
        style.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        style.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        return style;
    }

    private StandardColumn createColumn(Double width) {
        StandardColumn column = new StandardColumn();
        column.setUUID(UUID.randomUUID());
        column.setWidth(width.intValue());
        return column;
    }

    public static String convertToJalaliStrForPDF(Timestamp timestamp) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return DateUtil.todayDate() + "---" + formattedTime;
    }
}
