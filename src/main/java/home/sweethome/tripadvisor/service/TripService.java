package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.TripDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.entity.Location;
import home.sweethome.tripadvisor.entity.Trip;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.entity.Weather;
import home.sweethome.tripadvisor.repository.TripRepository;
import home.sweethome.tripadvisor.util.LocationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        trip.setRouteName(tripDTO.getFromAddress() + "-" + tripDTO.getToAddress());
        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "success, your route: " + trip.getRouteName()));
    }

    public TripResponseDTO getInfoTrip(String nameRoute) {
        if (tripRepository.findByRouteNameIgnoreCase(nameRoute).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found!");

        Trip trip = tripRepository.findByRouteNameIgnoreCase(nameRoute).get();
        List<Location> locationList = trip.getLocationList();
        List<Weather> weatherList = locationList.stream().flatMap(l -> l.getWeather().stream()).toList();

        StringBuilder lw = new StringBuilder();
        locationList.forEach(l -> {
            lw.append(l.toString());
            weatherList.stream().filter(w-> w.getLocation().equals(l)).forEach(lw::append);
        });

        return TripResponseDTO.builder()
                .duration(trip.getDuration())
                .routeName(trip.getRouteName())
                .infoLocation(lw.toString())
                .build();
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
