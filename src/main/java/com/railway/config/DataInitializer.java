package com.railway.config;

import com.railway.entity.ERole;
import com.railway.entity.Role;
import com.railway.entity.Station;
import com.railway.entity.Train;
import com.railway.entity.User;
import com.railway.repository.RoleRepository;
import com.railway.repository.StationRepository;
import com.railway.repository.TrainRepository;
import com.railway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        // Seed Admin User
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@railconnect.com", passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPhone("9999999999");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow());
            adminRoles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow());
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }

        // Seed Stations
        if (stationRepository.count() == 0) {
            Station ndls = createStation("NDLS", "New Delhi", "New Delhi", "Delhi");
            Station mmct = createStation("MMCT", "Mumbai Central", "Mumbai", "Maharashtra");
            Station csmt = createStation("CSMT", "Chhatrapati Shivaji Terminus", "Mumbai", "Maharashtra");
            Station hwh = createStation("HWH", "Howrah Junction", "Kolkata", "West Bengal");
            Station mas = createStation("MAS", "Chennai Central", "Chennai", "Tamil Nadu");
            Station sbc = createStation("SBC", "KSR Bengaluru", "Bengaluru", "Karnataka");
            Station jp = createStation("JP", "Jaipur Junction", "Jaipur", "Rajasthan");
            Station adi = createStation("ADI", "Ahmedabad Junction", "Ahmedabad", "Gujarat");
            Station lko = createStation("LKO", "Lucknow Charbagh", "Lucknow", "Uttar Pradesh");
            Station pnbe = createStation("PNBE", "Patna Junction", "Patna", "Bihar");

            // Seed Trains
            if (trainRepository.count() == 0) {
                createTrain("12952", "Mumbai Rajdhani", ndls, mmct, LocalTime.of(16, 55), LocalTime.of(8, 35), "15h 40m", 500, 2500.0, "Rajdhani");
                createTrain("12951", "Mumbai Rajdhani Return", mmct, ndls, LocalTime.of(17, 40), LocalTime.of(8, 35), "14h 55m", 500, 2500.0, "Rajdhani");
                createTrain("12301", "Howrah Rajdhani", ndls, hwh, LocalTime.of(16, 50), LocalTime.of(9, 55), "17h 5m", 600, 2800.0, "Rajdhani");
                createTrain("12302", "Howrah Rajdhani Return", hwh, ndls, LocalTime.of(14, 5), LocalTime.of(10, 0), "19h 55m", 600, 2800.0, "Rajdhani");
                createTrain("12621", "Tamil Nadu Express", ndls, mas, LocalTime.of(22, 30), LocalTime.of(7, 10), "32h 40m", 700, 1200.0, "Superfast");
                createTrain("12622", "Tamil Nadu Express Return", mas, ndls, LocalTime.of(22, 0), LocalTime.of(6, 40), "32h 40m", 700, 1200.0, "Superfast");
                createTrain("12627", "Karnataka Express", ndls, sbc, LocalTime.of(21, 15), LocalTime.of(6, 40), "33h 25m", 650, 1350.0, "Superfast");
                createTrain("12431", "Trivandrum Rajdhani", ndls, mas, LocalTime.of(10, 55), LocalTime.of(5, 15), "42h 20m", 400, 3200.0, "Rajdhani");
                createTrain("12903", "Golden Temple Mail", mmct, adi, LocalTime.of(21, 0), LocalTime.of(5, 30), "8h 30m", 500, 650.0, "Mail/Express");
                createTrain("12309", "Rajdhani Express", ndls, jp, LocalTime.of(5, 40), LocalTime.of(10, 30), "4h 50m", 450, 900.0, "Rajdhani");
                createTrain("12003", "Lucknow Shatabdi", ndls, lko, LocalTime.of(6, 10), LocalTime.of(12, 40), "6h 30m", 500, 1100.0, "Shatabdi");
                createTrain("12004", "Lucknow Shatabdi Return", lko, ndls, LocalTime.of(15, 15), LocalTime.of(21, 45), "6h 30m", 500, 1100.0, "Shatabdi");
                createTrain("12305", "Kolkata Rajdhani", ndls, hwh, LocalTime.of(17, 0), LocalTime.of(10, 10), "17h 10m", 550, 2650.0, "Rajdhani");
                createTrain("22691", "Bengaluru Rajdhani", ndls, sbc, LocalTime.of(20, 50), LocalTime.of(6, 40), "33h 50m", 400, 3000.0, "Rajdhani");
                createTrain("12839", "Chennai Mail", hwh, mas, LocalTime.of(23, 50), LocalTime.of(4, 35), "28h 45m", 600, 900.0, "Mail/Express");
            }
        }
    }

    private Station createStation(String code, String name, String city, String state) {
        Station s = new Station();
        s.setStationCode(code);
        s.setStationName(name);
        s.setCity(city);
        s.setState(state);
        return stationRepository.save(s);
    }

    private void createTrain(String number, String name, Station source, Station dest,
                             LocalTime departure, LocalTime arrival, String duration,
                             int seats, Double fare, String type) {
        Train t = new Train();
        t.setTrainNumber(number);
        t.setTrainName(name);
        t.setSourceStation(source);
        t.setDestinationStation(dest);
        t.setDepartureTime(departure);
        t.setArrivalTime(arrival);
        t.setDuration(duration);
        t.setTotalSeats(seats);
        t.setAvailableSeats(seats);
        t.setTicketFare(fare);
        t.setTrainType(type);
        trainRepository.save(t);
    }
}
