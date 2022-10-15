package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.TripRequestDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.dto.WeatherDTO;
import home.sweethome.tripadvisor.entity.Location;
import home.sweethome.tripadvisor.entity.Trip;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.entity.Weather;
import home.sweethome.tripadvisor.exceptionhandler.exception.TripServiceException;
import home.sweethome.tripadvisor.repository.TripRepository;
import home.sweethome.tripadvisor.util.DistanceUtil;
import home.sweethome.tripadvisor.util.LocationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        User user = getUser();
        String routeName = user.getUsername().hashCode() + "-" + tripRequestDTO.getFromAddress().substring(0, 2)
                + "-" + tripRequestDTO.getToAddress().substring(0, 2);
        if (tripRepository.findByRouteNameIgnoreCase(routeName).isPresent())
            throw new TripServiceException(HttpStatus.CONFLICT, "Trip with route name " + routeName
                    + " already exists!");

        LocalDate localDate = LocalDate.parse(tripRequestDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (localDate.isBefore(LocalDate.now()))
            throw new TripServiceException(HttpStatus.BAD_REQUEST, "Wrong start date");

        Trip trip = new Trip();

        List<Location> locationList = getLocationList(tripRequestDTO, trip);
        int distance = (int) DistanceUtil.distanceFromLocations(locationList);

        trip.setLocationList(locationList);
        trip.setStartDate(Date.valueOf(localDate));
        trip.setDuration(tripRequestDTO.getDuration());
        trip.setDistance(distance);
        trip.setUser(user);
        trip.setRouteName(routeName);
        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "success, your route: " + trip.getRouteName()));
    }

    public ResponseEntity<TripResponseDTO> getInfoTrip(String nameRoute) {
        if (tripRepository.findByRouteNameIgnoreCase(nameRoute).isEmpty())
            throw new TripServiceException(HttpStatus.NOT_FOUND, "Trip not found!");

        Trip trip = tripRepository.findByRouteNameIgnoreCase(nameRoute).get();
        List<Location> locationList = trip.getLocationList();

        return ResponseEntity.ok(TripResponseDTO.builder()
                .duration(trip.getDuration())
                .distance(trip.getDistance())
                .startDate(trip.getStartDate().toString())
                .routeName(trip.getRouteName())
                .infoLocationFromTO(locationList
                        .stream()
                        .map(Location::getName)
                        .collect(Collectors.joining(" - ")))
                .build());
    }

    public ResponseEntity<List<WeatherDTO>> getForecastTrip(String nameRoute) {

        Trip trip = getTrip(nameRoute);
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

    public ResponseEntity<Map<String, String>> changeDuration(String nameRoute, int newDuration) {
        Trip trip = getTrip(nameRoute);

        if (trip.getDuration() == newDuration)
            throw new TripServiceException(HttpStatus.CONFLICT, "This duration already set!");

        trip.setDuration(newDuration);
        tripRepository.save(trip);

        return ResponseEntity.ok().body(Collections.singletonMap(
                "status", "Duration successfully changed on " + newDuration + " days."));
    }

    public ResponseEntity<Map<String, String>> deleteTrip(String route, Principal principal) {
        Trip trip = getTrip(route);
        User user = userService.getByUsername(principal.getName());

        if (!user.equals(trip.getUser()))
            throw new TripServiceException(HttpStatus.FORBIDDEN
                    , "Wrong user, you don't have access to delete this trip!");

        tripRepository.delete(trip);

        return ResponseEntity.ok().body(Collections.singletonMap("status", "Trip with rout "
                + route + " successfully deleted!"));
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

    private Trip getTrip(String nameRoute) {
        return tripRepository.findByRouteNameIgnoreCase(nameRoute).orElseThrow(() ->
                new TripServiceException(HttpStatus.NOT_FOUND, "Trip not found!"));
    }

}
