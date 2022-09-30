package home.sweethome.fussdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TripDTO {
    @JsonProperty("from")
    private String fromAddress;
    @JsonProperty("to")
    private String toAddress;
    private int duration;
}
