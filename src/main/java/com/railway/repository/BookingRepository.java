package com.railway.repository;

import com.railway.entity.Booking;
import com.railway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    Optional<Booking> findByPnrNumber(String pnrNumber);
    Optional<Booking> findByBookingId(String bookingId);
}
