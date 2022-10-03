package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.TripDTO;
import home.sweethome.tripadvisor.entity.Location;
import home.sweethome.tripadvisor.entity.Route;
import home.sweethome.tripadvisor.entity.Trip;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.repository.TripRepository;
import home.sweethome.tripadvisor.util.LocationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TripService {
    private final LocationConverter locationConverter;
    private final TripRepository tripRepository;
    private final WeatherService weatherService;

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
