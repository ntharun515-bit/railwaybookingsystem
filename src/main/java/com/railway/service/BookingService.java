package com.railway.service;

import com.railway.dto.request.BookingRequest;
import com.railway.entity.Booking;
import com.railway.entity.Passenger;
import com.railway.entity.Train;
import com.railway.entity.User;
import com.railway.repository.BookingRepository;
import com.railway.repository.TrainRepository;
import com.railway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private TrainRepository trainRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Booking createBooking(String username, BookingRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        int numPassengers = request.getPassengers().size();
        if (train.getAvailableSeats() < numPassengers) {
            throw new RuntimeException("Not enough seats available. Available: " + train.getAvailableSeats());
        }

        Booking booking = new Booking();
        booking.setBookingId("BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setPnrNumber("PNR" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        booking.setUser(user);
        booking.setTrain(train);
        booking.setJourneyDate(request.getJourneyDate());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("PENDING");
        
        Double totalAmount = train.getTicketFare() * numPassengers;
        booking.setTotalAmount(totalAmount);

        // Assign seat numbers and link passengers
        int seatStart = train.getTotalSeats() - train.getAvailableSeats() + 1;
        for (int i = 0; i < numPassengers; i++) {
            Passenger p = request.getPassengers().get(i);
            p.setId(null); // Ensure JPA treats these as new entities
            p.setBooking(booking);
            p.setSeatNumber("S" + (seatStart + i));
            booking.getPassengers().add(p);
        }

        return bookingRepository.save(booking);
    }
    
    public List<Booking> getUserBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
                
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        // Return seats to train
        Train train = booking.getTrain();
        train.setAvailableSeats(train.getAvailableSeats() + booking.getPassengers().size());
        trainRepository.save(train);
        
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}
