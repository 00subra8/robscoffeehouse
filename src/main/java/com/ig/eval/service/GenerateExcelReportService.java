package com.ig.eval.service;

import com.ig.eval.exception.CoffeeHouseInternalException;
import com.ig.eval.model.Report;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.collections4.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

public class GenerateExcelReportService {

    public WritableWorkbook createExcelReport(HttpServletResponse response, List<Report> reportList) {
        String fileName = "Excel_Report.xls";
        WritableWorkbook writableWorkbook;
        try {
            response.setContentType("application/vnd.ms-excel");

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

            WritableSheet excelOutputSheet = writableWorkbook.createSheet("Report", 0);
            addExcelOutputHeader(excelOutputSheet);
            writeExcelOutputData(excelOutputSheet, reportList);

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception e) {
            throw new CoffeeHouseInternalException("Error occurred while creating Excel report: " + e.getMessage());
        }

        return writableWorkbook;
    }


    private void addExcelOutputHeader(WritableSheet sheet) throws WriteException {
        sheet.addCell(new Label(0, 0, " DATE"));
        sheet.addCell(new Label(1, 0, "VARIETY"));
        sheet.addCell(new Label(2, 0, "INITIAL AVAILABILITY"));
        sheet.addCell(new Label(3, 0, " QUANTITY SOLD"));
    }

    private void writeExcelOutputData(WritableSheet sheet, List<Report> reportList) {

        CollectionUtils.emptyIfNull(reportList).stream()
                .filter(Objects::nonNull)
                .forEach(report -> {
                    try {
                        writeReportData(sheet, report);
                    } catch (WriteException e) {
                        throw new CoffeeHouseInternalException("Error occurred while creating Excel report: " + e.getMessage());
                    }
                });


    }

    private void writeReportData(WritableSheet sheet, Report report) throws WriteException {
        int currentRow = sheet.getRows();
        sheet.addCell(new Label(0, currentRow, report.getDate()));
        sheet.addCell(new Label(1, currentRow, report.getVariety()));
        sheet.addCell(new Label(2, currentRow, report.getInitialAvailability()));
        sheet.addCell(new Label(3, currentRow, report.getQuantitySold()));
    }
}
