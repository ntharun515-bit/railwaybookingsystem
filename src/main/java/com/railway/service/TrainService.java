package com.railway.service;

import com.railway.entity.Station;
import com.railway.entity.Train;
import com.railway.repository.StationRepository;
import com.railway.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainService {
    @Autowired
    private TrainRepository trainRepository;
    
    @Autowired
    private StationRepository stationRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
    
    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }
    
    public Train updateTrain(Long id, Train trainDetails) {
        Train train = trainRepository.findById(id).orElseThrow(() -> new RuntimeException("Train not found"));
        train.setTrainName(trainDetails.getTrainName());
        train.setTrainNumber(trainDetails.getTrainNumber());
        train.setSourceStation(trainDetails.getSourceStation());
        train.setDestinationStation(trainDetails.getDestinationStation());
        train.setDepartureTime(trainDetails.getDepartureTime());
        train.setArrivalTime(trainDetails.getArrivalTime());
        train.setDuration(trainDetails.getDuration());
        train.setTotalSeats(trainDetails.getTotalSeats());
        train.setAvailableSeats(trainDetails.getAvailableSeats());
        train.setTicketFare(trainDetails.getTicketFare());
        train.setTrainType(trainDetails.getTrainType());
        return trainRepository.save(train);
    }
    
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    public List<Train> searchTrains(String sourceCode, String destinationCode) {
        Optional<Station> source = stationRepository.findByStationCode(sourceCode);
        Optional<Station> dest = stationRepository.findByStationCode(destinationCode);
        
        if (source.isPresent() && dest.isPresent()) {
            return trainRepository.findBySourceStationAndDestinationStation(source.get(), dest.get());
        }
        return List.of();
    }
}
