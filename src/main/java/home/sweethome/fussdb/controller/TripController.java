package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.util.LocationConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/trip")
public class TripController {

    @GetMapping("/address")
    public ResponseEntity getLoc(@RequestParam String address) throws IOException {
       return ResponseEntity.ok().body(LocationConverter.StringAddressToGeocode(address));
    }

}
