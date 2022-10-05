package home.sweethome.tripadvisor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherDTO {

    private String date;
    private String location;
    private int minTemp;
    private int maxTemp;

}
