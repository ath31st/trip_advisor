package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.TripRequestDTO;
import home.sweethome.tripadvisor.dto.TripResponseDTO;
import home.sweethome.tripadvisor.dto.WeatherDTO;
import home.sweethome.tripadvisor.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/trip")
public class TripController {
    private final TripService tripService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> tripHandler(@Valid @RequestBody TripRequestDTO tripRequestDTO) {
        return tripService.newTrip(tripRequestDTO);
    }

    @GetMapping("/route-info/{route}")
    public ResponseEntity<TripResponseDTO> tripInfo(@PathVariable @NotBlank String route) {
        return tripService.getInfoTrip(route);
    }

    @GetMapping("/weather/{route}")
    public ResponseEntity<List<WeatherDTO>> tripForecastInfo(@PathVariable @NotBlank String route) {
        return tripService.getForecastTrip(route);
    }

    @PostMapping("/change-duration/{route}/")
    public ResponseEntity<Map<String, String>> durationHandler(@PathVariable @NotBlank String route,
                                                                @RequestParam @Min(1) @Max(30) int duration) {
        return tripService.changeDuration(route, duration);
    }

    @DeleteMapping("/delete-trip/{route}")
    public ResponseEntity<Map<String,String>> deleteTrip(@PathVariable @NotBlank String route,
                                                         Principal principal){
        return tripService.deleteTrip(route, principal);
    }

}
