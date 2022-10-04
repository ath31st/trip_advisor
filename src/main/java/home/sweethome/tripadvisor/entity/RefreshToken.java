package home.sweethome.tripadvisor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document("refreshtokens")
@Data
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String username;
    private String token;
}
