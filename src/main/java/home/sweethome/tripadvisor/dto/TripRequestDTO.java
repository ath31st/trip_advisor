package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TripRequestDTO {

    @JsonProperty("from")
    @NotBlank
    private String fromAddress;
    @JsonProperty("to")
    @NotBlank
    private String toAddress;
    private int duration;

}
