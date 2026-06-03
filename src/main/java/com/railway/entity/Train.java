package com.railway.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "trains",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "trainNumber")
    })
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainNumber;
    private String trainName;
    
    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;
    
    @ManyToOne
    @JoinColumn(name = "destination_station_id")
    private Station destinationStation;
    
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String duration;
    
    private Integer totalSeats;
    private Integer availableSeats;
    private Double ticketFare;
    private String trainType; // e.g., Express, Superfast, Passenger

    public Train() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public Station getSourceStation() { return sourceStation; }
    public void setSourceStation(Station sourceStation) { this.sourceStation = sourceStation; }
    public Station getDestinationStation() { return destinationStation; }
    public void setDestinationStation(Station destinationStation) { this.destinationStation = destinationStation; }
    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    public Double getTicketFare() { return ticketFare; }
    public void setTicketFare(Double ticketFare) { this.ticketFare = ticketFare; }
    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }
}
