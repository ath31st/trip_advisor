package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.TripDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip")
public class TripController {
    private final TripService tripService;

    @PostMapping("/new")
    public ResponseEntity<Map<String,String>> tripHandler(@RequestBody TripDTO tripDTO) {
        return tripService.newTrip(tripDTO);
    }

    @GetMapping("/route/{route}")
    public TripResponseDTO tripInfoHandler(@PathVariable String route){
        return tripService.getInfoTrip(route);
    }
}
