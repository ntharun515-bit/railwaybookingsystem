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
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


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
        Station ndls = getOrCreateStation("NDLS", "New Delhi", "New Delhi", "Delhi");
        Station mmct = getOrCreateStation("MMCT", "Mumbai Central", "Mumbai", "Maharashtra");
        Station csmt = getOrCreateStation("CSMT", "Chhatrapati Shivaji Terminus", "Mumbai", "Maharashtra");
        Station hwh = getOrCreateStation("HWH", "Howrah Junction", "Kolkata", "West Bengal");
        Station mas = getOrCreateStation("MAS", "Chennai Central", "Chennai", "Tamil Nadu");
        Station sbc = getOrCreateStation("SBC", "KSR Bengaluru", "Bengaluru", "Karnataka");
        Station jp = getOrCreateStation("JP", "Jaipur Junction", "Jaipur", "Rajasthan");
        Station adi = getOrCreateStation("ADI", "Ahmedabad Junction", "Ahmedabad", "Gujarat");
        Station lko = getOrCreateStation("LKO", "Lucknow Charbagh", "Lucknow", "Uttar Pradesh");
        Station pnbe = getOrCreateStation("PNBE", "Patna Junction", "Patna", "Bihar");

        // Seed Trains
        createTrainIfNotExists("12952", "Mumbai Rajdhani", ndls, mmct, LocalTime.of(16, 55), LocalTime.of(8, 35), "15h 40m", 500, 2500.0, "Rajdhani");
        createTrainIfNotExists("12951", "Mumbai Rajdhani Return", mmct, ndls, LocalTime.of(17, 40), LocalTime.of(8, 35), "14h 55m", 500, 2500.0, "Rajdhani");
        createTrainIfNotExists("12301", "Howrah Rajdhani", ndls, hwh, LocalTime.of(16, 50), LocalTime.of(9, 55), "17h 5m", 600, 2800.0, "Rajdhani");
        createTrainIfNotExists("12302", "Howrah Rajdhani Return", hwh, ndls, LocalTime.of(14, 5), LocalTime.of(10, 0), "19h 55m", 600, 2800.0, "Rajdhani");
        createTrainIfNotExists("12621", "Tamil Nadu Express", ndls, mas, LocalTime.of(22, 30), LocalTime.of(7, 10), "32h 40m", 700, 1200.0, "Superfast");
        createTrainIfNotExists("12622", "Tamil Nadu Express Return", mas, ndls, LocalTime.of(22, 0), LocalTime.of(6, 40), "32h 40m", 700, 1200.0, "Superfast");
        createTrainIfNotExists("12627", "Karnataka Express", ndls, sbc, LocalTime.of(21, 15), LocalTime.of(6, 40), "33h 25m", 650, 1350.0, "Superfast");
        createTrainIfNotExists("12431", "Trivandrum Rajdhani", ndls, mas, LocalTime.of(10, 55), LocalTime.of(5, 15), "42h 20m", 400, 3200.0, "Rajdhani");
        createTrainIfNotExists("12903", "Golden Temple Mail", mmct, adi, LocalTime.of(21, 0), LocalTime.of(5, 30), "8h 30m", 500, 650.0, "Mail/Express");
        createTrainIfNotExists("12309", "Rajdhani Express", ndls, jp, LocalTime.of(5, 40), LocalTime.of(10, 30), "4h 50m", 450, 900.0, "Rajdhani");
        createTrainIfNotExists("12003", "Lucknow Shatabdi", ndls, lko, LocalTime.of(6, 10), LocalTime.of(12, 40), "6h 30m", 500, 1100.0, "Shatabdi");
        createTrainIfNotExists("12004", "Lucknow Shatabdi Return", lko, ndls, LocalTime.of(15, 15), LocalTime.of(21, 45), "6h 30m", 500, 1100.0, "Shatabdi");
        createTrainIfNotExists("12305", "Kolkata Rajdhani", ndls, hwh, LocalTime.of(17, 0), LocalTime.of(10, 10), "17h 10m", 550, 2650.0, "Rajdhani");
        createTrainIfNotExists("22691", "Bengaluru Rajdhani", ndls, sbc, LocalTime.of(20, 50), LocalTime.of(6, 40), "33h 50m", 400, 3000.0, "Rajdhani");
        createTrainIfNotExists("12839", "Chennai Mail", hwh, mas, LocalTime.of(23, 50), LocalTime.of(4, 35), "28h 45m", 600, 900.0, "Mail/Express");

        // Returns for existing trains
        createTrainIfNotExists("12628", "Karnataka Express Return", sbc, ndls, LocalTime.of(19, 20), LocalTime.of(4, 40), "33h 20m", 650, 1350.0, "Superfast");
        createTrainIfNotExists("12432", "Trivandrum Rajdhani Return", mas, ndls, LocalTime.of(15, 30), LocalTime.of(9, 50), "42h 20m", 400, 3200.0, "Rajdhani");
        createTrainIfNotExists("12904", "Golden Temple Mail Return", adi, mmct, LocalTime.of(22, 30), LocalTime.of(7, 0), "8h 30m", 500, 650.0, "Mail/Express");
        createTrainIfNotExists("12310", "Rajdhani Express Return", jp, ndls, LocalTime.of(18, 0), LocalTime.of(22, 50), "4h 50m", 450, 900.0, "Rajdhani");
        createTrainIfNotExists("12306", "Kolkata Rajdhani Return", hwh, ndls, LocalTime.of(16, 50), LocalTime.of(10, 0), "17h 10m", 550, 2650.0, "Rajdhani");
        createTrainIfNotExists("22692", "Bengaluru Rajdhani Return", sbc, ndls, LocalTime.of(20, 0), LocalTime.of(5, 55), "33h 55m", 400, 3000.0, "Rajdhani");
        createTrainIfNotExists("12840", "Chennai Mail Return", mas, hwh, LocalTime.of(23, 45), LocalTime.of(4, 30), "28h 45m", 600, 900.0, "Mail/Express");

        // CSMT and PNBE routes
        createTrainIfNotExists("12260", "CSMT Howrah Duronto", csmt, hwh, LocalTime.of(17, 15), LocalTime.of(16, 15), "23h 0m", 500, 2700.0, "Duronto");
        createTrainIfNotExists("12259", "CSMT Howrah Duronto Return", hwh, csmt, LocalTime.of(16, 15), LocalTime.of(15, 15), "23h 0m", 500, 2700.0, "Duronto");
        createTrainIfNotExists("12137", "Punjab Mail", csmt, ndls, LocalTime.of(19, 35), LocalTime.of(21, 50), "26h 15m", 700, 850.0, "Mail/Express");
        createTrainIfNotExists("12138", "Punjab Mail Return", ndls, csmt, LocalTime.of(5, 15), LocalTime.of(7, 35), "26h 20m", 700, 850.0, "Mail/Express");
        createTrainIfNotExists("12393", "Sampoorna Kranti Express", pnbe, ndls, LocalTime.of(19, 25), LocalTime.of(7, 55), "12h 30m", 800, 700.0, "Superfast");
        createTrainIfNotExists("12394", "Sampoorna Kranti Express Return", ndls, pnbe, LocalTime.of(17, 30), LocalTime.of(6, 0), "12h 30m", 800, 700.0, "Superfast");
        createTrainIfNotExists("12296", "Sanghamitra Express", pnbe, sbc, LocalTime.of(20, 15), LocalTime.of(17, 40), "45h 25m", 650, 1100.0, "Superfast");
        createTrainIfNotExists("12295", "Sanghamitra Express Return", sbc, pnbe, LocalTime.of(9, 15), LocalTime.of(6, 40), "45h 25m", 650, 1100.0, "Superfast");

        // Connecting routes
        createTrainIfNotExists("12863", "Howrah Bengaluru Express", hwh, sbc, LocalTime.of(22, 55), LocalTime.of(6, 45), "31h 50m", 600, 950.0, "Superfast");
        createTrainIfNotExists("12864", "Howrah Bengaluru Express Return", sbc, hwh, LocalTime.of(10, 35), LocalTime.of(18, 30), "31h 55m", 600, 950.0, "Superfast");
        createTrainIfNotExists("12955", "Mumbai Jaipur Superfast", mmct, jp, LocalTime.of(18, 50), LocalTime.of(12, 45), "17h 55m", 600, 900.0, "Superfast");
        createTrainIfNotExists("12956", "Mumbai Jaipur Superfast Return", jp, mmct, LocalTime.of(14, 0), LocalTime.of(7, 40), "17h 40m", 600, 900.0, "Superfast");
        createTrainIfNotExists("14234", "Lucknow Patna Express", lko, pnbe, LocalTime.of(8, 30), LocalTime.of(16, 30), "8h 0m", 500, 450.0, "Mail/Express");
        createTrainIfNotExists("14233", "Lucknow Patna Express Return", pnbe, lko, LocalTime.of(9, 45), LocalTime.of(17, 45), "8h 0m", 500, 450.0, "Mail/Express");
        createTrainIfNotExists("12916", "Ashram Express", jp, adi, LocalTime.of(20, 25), LocalTime.of(5, 30), "9h 5m", 600, 600.0, "Superfast");
        createTrainIfNotExists("12915", "Ashram Express Return", adi, jp, LocalTime.of(18, 30), LocalTime.of(3, 25), "8h 55m", 600, 600.0, "Superfast");
        createTrainIfNotExists("12607", "Lalbagh Express", mas, sbc, LocalTime.of(15, 30), LocalTime.of(21, 35), "6h 5m", 500, 400.0, "Superfast");
        createTrainIfNotExists("12608", "Lalbagh Express Return", sbc, mas, LocalTime.of(6, 20), LocalTime.of(12, 15), "5h 55m", 500, 400.0, "Superfast");

        // Seed 500 programmatic trains
        List<Station> stations = List.of(ndls, mmct, csmt, hwh, mas, sbc, jp, adi, lko, pnbe);
        String[] trainTypes = {"Express", "Superfast", "Rajdhani", "Shatabdi", "Mail/Express", "Duronto"};
        String[] prefixes = {"Humsafar", "Tejas", "Gatimaan", "Vande Bharat", "Garib Rath", "Jan Shatabdi", "Double Decker", "Yuva", "Antyodaya", "Uday"};
        
        Random rand = new Random(42); // deterministic random seed
        
        int generatedCount = 0;
        while (generatedCount < 500) {
            Station source = stations.get(rand.nextInt(stations.size()));
            Station dest = stations.get(rand.nextInt(stations.size()));
            if (source.equals(dest)) continue;
            
            // Deterministic train number (5 digits)
            int num = 10000 + rand.nextInt(90000);
            String trainNumber = String.valueOf(num);
            
            String type = trainTypes[rand.nextInt(trainTypes.length)];
            String prefix = prefixes[rand.nextInt(prefixes.length)];
            String name = source.getCity() + " - " + dest.getCity() + " " + prefix + " " + type;
            
            int depHour = rand.nextInt(24);
            int depMin = rand.nextInt(12) * 5;
            LocalTime departure = LocalTime.of(depHour, depMin);
            
            int durationHours = 2 + rand.nextInt(40);
            int durationMins = rand.nextInt(12) * 5;
            LocalTime arrival = departure.plusHours(durationHours).plusMinutes(durationMins);
            String duration = durationHours + "h " + durationMins + "m";
            
            int seats = 100 + rand.nextInt(15) * 50;
            double baseFare = 300.0 + rand.nextInt(40) * 50.0;
            if ("Rajdhani".equals(type) || "Shatabdi".equals(type) || "Duronto".equals(type)) {
                baseFare *= 1.8;
            } else if ("Superfast".equals(type)) {
                baseFare *= 1.2;
            }
            
            createTrainIfNotExists(trainNumber, name, source, dest, departure, arrival, duration, seats, baseFare, type);
            generatedCount++;
        }
    }


    private Station getOrCreateStation(String code, String name, String city, String state) {
        return stationRepository.findByStationCode(code)
                .orElseGet(() -> createStation(code, name, city, state));
    }

    private Station createStation(String code, String name, String city, String state) {
        Station s = new Station();
        s.setStationCode(code);
        s.setStationName(name);
        s.setCity(city);
        s.setState(state);
        return stationRepository.save(s);
    }

    private void createTrainIfNotExists(String number, String name, Station source, Station dest,
                                        LocalTime departure, LocalTime arrival, String duration,
                                        int seats, Double fare, String type) {
        if (!trainRepository.existsByTrainNumber(number)) {
            createTrain(number, name, source, dest, departure, arrival, duration, seats, fare, type);
        }
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

