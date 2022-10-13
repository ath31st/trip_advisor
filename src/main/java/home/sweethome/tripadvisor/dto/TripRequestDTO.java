package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class TripRequestDTO {

    @JsonProperty("from")
    @NotBlank
    private String fromAddress;
    @JsonProperty("to")
    @NotBlank
    private String toAddress;
    @Min(1)
    @Max(30)
    private int duration;

}
