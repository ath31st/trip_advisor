package home.sweethome.fussdb.service;

import home.sweethome.fussdb.repository.TripRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }
}
