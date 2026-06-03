package com.railway.service;

import com.railway.dto.request.PaymentRequest;
import com.railway.entity.Booking;
import com.railway.entity.Payment;
import com.railway.entity.Ticket;
import com.railway.entity.Train;
import com.railway.repository.BookingRepository;
import com.railway.repository.PaymentRepository;
import com.railway.repository.TicketRepository;
import com.railway.repository.TrainRepository;
import com.railway.util.PdfGenerator;
import com.railway.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
                
        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already processed");
        }
        
        if (!booking.getTotalAmount().equals(request.getAmount())) {
            throw new RuntimeException("Invalid payment amount");
        }
        
        // Simulate payment processing...
        boolean paymentSuccess = true; 
        
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId("TXN" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        payment.setPaymentTime(LocalDateTime.now());
        
        if (paymentSuccess) {
            payment.setStatus("SUCCESS");
            booking.setStatus("CONFIRMED");
            
            // Deduct seats
            Train train = booking.getTrain();
            train.setAvailableSeats(train.getAvailableSeats() - booking.getPassengers().size());
            trainRepository.save(train);

            try {
                // Generate QR Code data
                String qrData = "PNR: " + booking.getPnrNumber() + "\n" +
                                "Train: " + train.getTrainNumber() + "\n" +
                                "Date: " + booking.getJourneyDate();
                byte[] qrCodeBytes = qrCodeGenerator.generateQrCode(qrData);

                // Generate PDF
                byte[] pdfBytes = pdfGenerator.generateTicketPdf(booking, qrCodeBytes);

                // Save Ticket
                Ticket ticket = new Ticket();
                ticket.setTicketNumber("TKT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                ticket.setBooking(booking);
                ticket.setQrCode(qrCodeBytes);
                ticket.setPdfContent(pdfBytes);
                ticket.setGeneratedTime(LocalDateTime.now());
                ticketRepository.save(ticket);
                
            } catch (Exception e) {
                // Log error but don't fail payment
                System.err.println("Failed to generate ticket PDF/QR: " + e.getMessage());
            }

        } else {
            payment.setStatus("FAILED");
            booking.setStatus("FAILED");
        }
        
        bookingRepository.save(booking);
        return paymentRepository.save(payment);
    }
    
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
}
