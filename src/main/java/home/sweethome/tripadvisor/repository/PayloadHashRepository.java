package home.sweethome.tripadvisor.repository;

import home.sweethome.tripadvisor.entity.PayloadHash;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PayloadHashRepository extends MongoRepository<PayloadHash, String> {

    @Query("{username:'?0'}")
    PayloadHash findByUsernameIgnoreCase(String username);

}
