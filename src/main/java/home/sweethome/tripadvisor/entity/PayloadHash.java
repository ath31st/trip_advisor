package home.sweethome.tripadvisor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("payloadhashes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadHash {

    @Id
    private String username;
    private String hash;

}
