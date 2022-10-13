package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDTO {

    @JsonProperty("old password")
    String oldPass;
    @JsonProperty("new password")
    String newPass;

}
