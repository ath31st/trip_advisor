package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.TripRequestDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.dto.WeatherDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {
    private final LocationConverter locationConverter;
    private final TripRepository tripRepository;
    private final WeatherService weatherService;
    private final UserService userService;

    public ResponseEntity<Map<String, String>> newTrip(TripRequestDTO tripRequestDTO) {

        Trip trip = new Trip();

        trip.setLocationList(getLocationList(tripRequestDTO, trip));
        trip.setDuration(tripRequestDTO.getDuration());
        trip.setUser(getUser());
        trip.setRouteName(tripRequestDTO.getFromAddress().substring(0, 2) + "-"
                + tripRequestDTO.getToAddress().substring(0, 2) + "-" + tripRequestDTO.getDuration());
        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "success, your route: " + trip.getRouteName()));
    }

    public ResponseEntity<TripResponseDTO> getInfoTrip(String nameRoute) {
        if (tripRepository.findByRouteNameIgnoreCase(nameRoute).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found!");

        Trip trip = tripRepository.findByRouteNameIgnoreCase(nameRoute).get();
        List<Location> locationList = trip.getLocationList();

        return ResponseEntity.ok(TripResponseDTO.builder()
                .duration(trip.getDuration())
                .routeName(trip.getRouteName())
                .infoLocationFromTO(locationList
                        .stream()
                        .map(Location::getName)
                        .collect(Collectors.joining(" - ")))
                .build());
    }

    public ResponseEntity<List<WeatherDTO>> getForecastTrip(String nameRoute) {
        if (tripRepository.findByRouteNameIgnoreCase(nameRoute).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found!");

        Trip trip = tripRepository.findByRouteNameIgnoreCase(nameRoute).get();
        List<Location> locationList = trip.getLocationList();
        List<Weather> weatherList = locationList.stream().flatMap(l -> l.getWeather().stream()).toList();

        List<WeatherDTO> list = weatherList
                .stream()
                .map(w -> WeatherDTO.builder()
                        .date(w.getDate())
                        .location(w.getLocation().getName())
                        .minTemp(w.getMinTemp())
                        .maxTemp(w.getMaxTemp())
                        .build())
                .toList();

        return ResponseEntity.ok(list);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getByUsername((String) authentication.getPrincipal());
    }

    private List<Location> getLocationList(TripRequestDTO tripRequestDTO, Trip trip) {
        List<Location> locationList = new ArrayList<>();

        try {
            Location locationFrom = locationConverter.stringAddressToGeocode(
                    convertInputStringAddress(tripRequestDTO.getFromAddress()));
            locationFrom.setWeather(weatherService.getForecast(locationFrom));
            locationFrom.setTrip(trip);
            locationList.add(locationFrom);

            Location locationTo = locationConverter.stringAddressToGeocode(
                    convertInputStringAddress(tripRequestDTO.getToAddress()));
            locationTo.setWeather(weatherService.getForecast(locationTo));
            locationTo.setTrip(trip);
            locationList.add(locationTo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationList;
    }

    private String convertInputStringAddress(String address) {
        return address.trim().replace(" ", "_");
    }

}
