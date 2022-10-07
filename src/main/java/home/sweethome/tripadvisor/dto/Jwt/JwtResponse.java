package home.sweethome.tripadvisor.dto.Jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    @JsonProperty("access token")
    private String accessToken;
    @JsonProperty("refresh token")
    private String refreshToken;

}