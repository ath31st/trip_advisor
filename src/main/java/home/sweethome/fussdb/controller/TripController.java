package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.dto.TripDTO;
import home.sweethome.fussdb.service.TripService;
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

}
