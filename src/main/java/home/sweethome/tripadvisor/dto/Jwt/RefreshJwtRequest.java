package home.sweethome.tripadvisor.dto.Jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    @JsonProperty("refresh token")
    public String refreshToken;

}