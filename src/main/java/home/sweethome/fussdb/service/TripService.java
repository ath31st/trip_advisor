package home.sweethome.fussdb.service;

import home.sweethome.fussdb.dto.TripDTO;
import home.sweethome.fussdb.entity.Location;
import home.sweethome.fussdb.entity.Route;
import home.sweethome.fussdb.entity.Trip;
import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.repository.TripRepository;
import home.sweethome.fussdb.util.LocationConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class TripService {
    private final LocationConverter locationConverter;
    private final TripRepository tripRepository;
    private final WeatherService weatherService;

    public TripService(LocationConverter locationConverter,
                       TripRepository tripRepository,
                       WeatherService weatherService) {
        this.locationConverter = locationConverter;
        this.tripRepository = tripRepository;
        this.weatherService = weatherService;
    }

    public ResponseEntity<Map<String, String>> newTrip(TripDTO tripDTO) {
        List<Location> locationList = new LinkedList<>();
        Route route = new Route();
        try {
            Location location = locationConverter.stringAddressToGeocode(tripDTO.getFromAddress());
            location.setWeather(weatherService.getForecast(location));
            locationList.add(location);
            location = locationConverter.stringAddressToGeocode(tripDTO.getToAddress());
            location.setWeather(weatherService.getForecast(location));
            locationList.add(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        route.setLocationList(locationList);

        Trip trip = new Trip();
        trip.setDuration(tripDTO.getDuration());
        trip.setUser(getUser());
        trip.setRoute(route);

        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "success"));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
