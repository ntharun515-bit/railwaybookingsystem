package com.railway.controller;

import com.railway.repository.BookingRepository;
import com.railway.repository.CancellationRepository;
import com.railway.repository.TrainRepository;
import com.railway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CancellationRepository cancellationRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        stats.put("totalTrains", trainRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("totalCancellations", cancellationRepository.count());
        
        return ResponseEntity.ok(stats);
    }
}
