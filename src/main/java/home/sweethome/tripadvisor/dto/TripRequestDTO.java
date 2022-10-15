package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class TripRequestDTO {

    @JsonProperty(value = "from", required = true)
    @NotBlank
    private String fromAddress;

    @JsonProperty(value = "to", required = true)
    @NotBlank
    private String toAddress;

    @JsonProperty(value = "start date", required = true)
    @NotBlank
    private String startDate;

    @Min(1)
    @Max(30)
    @JsonProperty(value = "duration", required = true)
    private int duration;

}
