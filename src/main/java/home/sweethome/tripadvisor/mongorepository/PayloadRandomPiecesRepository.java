package home.sweethome.tripadvisor.mongorepository;

import home.sweethome.tripadvisor.entity.PayloadRandomPiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PayloadRandomPiecesRepository extends MongoRepository<PayloadRandomPiece, String> {

    @Query("{username:'?0'}")
    PayloadRandomPiece findByUsernameIgnoreCase(String username);

}
