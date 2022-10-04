package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripResponseDTO {

    @JsonProperty("route name")
    private String routeName;
    private int duration;
    @JsonProperty("info location")
    private String infoLocation;

}
