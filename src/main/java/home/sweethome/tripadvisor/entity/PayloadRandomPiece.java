package home.sweethome.tripadvisor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("payloadpieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadRandomPiece {

    @Id
    private String username;
    private String uuid;

}
