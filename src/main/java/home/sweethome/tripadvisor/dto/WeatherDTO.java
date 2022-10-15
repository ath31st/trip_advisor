package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherDTO {

    private String date;

    private String location;

    @JsonProperty("minimum temperature")
    private int minTemp;

    @JsonProperty("maximum temperature")
    private int maxTemp;

}
