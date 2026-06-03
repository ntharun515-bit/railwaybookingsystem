package com.railway.controller;

import com.railway.entity.Station;
import com.railway.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Station createStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Station> updateStation(@PathVariable Long id, @RequestBody Station stationDetails) {
        return stationRepository.findById(id)
                .map(station -> {
                    station.setStationName(stationDetails.getStationName());
                    station.setStationCode(stationDetails.getStationCode());
                    station.setCity(stationDetails.getCity());
                    station.setState(stationDetails.getState());
                    return ResponseEntity.ok(stationRepository.save(station));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
