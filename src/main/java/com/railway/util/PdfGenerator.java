package com.railway.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.railway.entity.Booking;
import com.railway.entity.Passenger;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfGenerator {

    public byte[] generateTicketPdf(Booking booking, byte[] qrCodeImageBytes) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Add Header
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE);
        Paragraph title = new Paragraph("RailConnect E-Ticket", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        document.add(Chunk.NEWLINE);

        // Add QR Code
        if (qrCodeImageBytes != null) {
            Image qrImage = Image.getInstance(qrCodeImageBytes);
            qrImage.setAlignment(Element.ALIGN_RIGHT);
            qrImage.scaleAbsolute(100, 100);
            document.add(qrImage);
        }

        // Booking Details Table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        addCell(table, "PNR Number: " + booking.getPnrNumber());
        addCell(table, "Booking ID: " + booking.getBookingId());
        addCell(table, "Train: " + booking.getTrain().getTrainName() + " (" + booking.getTrain().getTrainNumber() + ")");
        addCell(table, "Journey Date: " + booking.getJourneyDate().toString());
        addCell(table, "Source: " + booking.getTrain().getSourceStation().getStationName());
        addCell(table, "Destination: " + booking.getTrain().getDestinationStation().getStationName());
        addCell(table, "Status: " + booking.getStatus());
        addCell(table, "Total Amount: Rs " + booking.getTotalAmount());
        
        document.add(table);

        // Passengers
        document.add(new Paragraph("Passenger Details:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(Chunk.NEWLINE);

        PdfPTable paxTable = new PdfPTable(4);
        paxTable.setWidthPercentage(100);
        
        addHeaderCell(paxTable, "Name");
        addHeaderCell(paxTable, "Age");
        addHeaderCell(paxTable, "Gender");
        addHeaderCell(paxTable, "Seat Preference");

        for (Passenger p : booking.getPassengers()) {
            paxTable.addCell(p.getName());
            paxTable.addCell(String.valueOf(p.getAge()));
            paxTable.addCell(p.getGender());
            paxTable.addCell(p.getSeatPreference() != null ? p.getSeatPreference() : "No Preference");
        }

        document.add(paxTable);
        document.close();

        return out.toByteArray();
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
