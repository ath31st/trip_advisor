package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.TripDTO;
import home.sweethome.tripadvisor.entity.Location;
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
    private final UserService userService;

    public ResponseEntity<Map<String, String>> newTrip(TripDTO tripDTO) {

        Trip trip = new Trip();

        trip.setLocationList(getLocationList(tripDTO, trip));
        trip.setDuration(tripDTO.getDuration());
        trip.setUser(getUser());

        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "success"));
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getByUsername((String) authentication.getPrincipal());
    }

    private List<Location> getLocationList(TripDTO tripDTO, Trip trip) {
        List<Location> locationList = new ArrayList<>();

        try {
            Location locationFrom = locationConverter.stringAddressToGeocode(tripDTO.getFromAddress());
            locationFrom.setWeather(weatherService.getForecast(locationFrom));
            locationFrom.setTrip(trip);
            locationList.add(locationFrom);

            Location locationTo = locationConverter.stringAddressToGeocode(tripDTO.getToAddress());
            locationTo.setWeather(weatherService.getForecast(locationTo));
            locationTo.setTrip(trip);
            locationList.add(locationTo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationList;
    }

}
