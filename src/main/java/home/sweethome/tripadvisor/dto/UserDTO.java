package home.sweethome.tripadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import home.sweethome.tripadvisor.util.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserDTO {

    private String firstname;

    private String lastname;

    private String username;

    @JsonProperty("register date")
    private LocalDateTime registerDate;

    private List<Role> roles;

}
