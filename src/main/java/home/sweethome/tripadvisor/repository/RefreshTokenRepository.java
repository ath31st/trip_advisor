package home.sweethome.tripadvisor.repository;

import home.sweethome.tripadvisor.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    @Query("{username:'?0'}")
    RefreshToken findByUsernameIgnoreCase(String username);
}
