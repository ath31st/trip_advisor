package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.TripRequestDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.dto.WeatherDTO;
import home.sweethome.tripadvisor.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip")
public class TripController {
    private final TripService tripService;

    @PostMapping("/new")
    public ResponseEntity<Map<String,String>> tripHandler(@RequestBody TripRequestDTO tripRequestDTO) {
        return tripService.newTrip(tripRequestDTO);
    }

    @GetMapping("/route/{route}")
    public ResponseEntity<TripResponseDTO> tripInfo(@PathVariable String route){
        return tripService.getInfoTrip(route);
    }

    @GetMapping("/weather/{route}")
    public ResponseEntity<List<WeatherDTO>> tripForecastInfo(@PathVariable String route){
        return tripService.getForecastTrip(route);
    }

}
