package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDTO {

    @JsonProperty(value = "old password", required = true)
    String oldPass;

    @JsonProperty(value = "new password", required = true)
    String newPass;

}
