package com.adith.walk.exporters;

import com.adith.walk.dto.SalesReportDto;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.List;


@Component
public class OrderPDFExporter {

    private final List<SalesReportDto> report;

    public OrderPDFExporter(List<SalesReportDto> report) {
        this.report = report;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("size", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("totalPrice", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("taxRate", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("finalPrice", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (SalesReportDto reportDto : report) {
            table.addCell(String.valueOf(reportDto.getId()));
            table.addCell(reportDto.getName());
            table.addCell(String.valueOf(reportDto.getSize()));
            table.addCell(String.valueOf(reportDto.getQuantity()));
            table.addCell(String.valueOf(reportDto.getTotalPrice()));
            table.addCell(String.valueOf(reportDto.getTaxRate()).concat("%"));
            table.addCell(String.valueOf(reportDto.getFinalPrice()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f, 1.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}