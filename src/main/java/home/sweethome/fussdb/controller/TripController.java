package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.dto.TripDTO;
import home.sweethome.fussdb.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/trip")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String,String>> tripHandler(@RequestBody TripDTO tripDTO) {
        return tripService.newTrip(tripDTO);
    }

}
