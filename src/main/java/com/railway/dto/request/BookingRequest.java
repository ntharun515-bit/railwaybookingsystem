package com.railway.dto.request;

import com.railway.entity.Passenger;
import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    private Long trainId;
    private LocalDate journeyDate;
    private List<Passenger> passengers;

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }
}
