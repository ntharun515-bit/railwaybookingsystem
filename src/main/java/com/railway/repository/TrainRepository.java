package com.railway.repository;

import com.railway.entity.Station;
import com.railway.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findBySourceStationAndDestinationStation(Station sourceStation, Station destinationStation);
    boolean existsByTrainNumber(String trainNumber);
}

