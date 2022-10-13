package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripResponseDTO {

    @JsonProperty("route name")
    private String routeName;
    @JsonProperty("start date")
    private String startDate;
    private int duration;
    @JsonProperty("info locations")
    private String infoLocationFromTO;

}
