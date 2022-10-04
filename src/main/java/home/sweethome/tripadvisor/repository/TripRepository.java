package home.sweethome.tripadvisor.repository;

import home.sweethome.tripadvisor.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByRouteNameIgnoreCase(String routeName);
}
