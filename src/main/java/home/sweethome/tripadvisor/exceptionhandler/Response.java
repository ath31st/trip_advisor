package home.sweethome.tripadvisor.exceptionhandler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class Response {

    String timestamp;
    HttpStatus status;
    String error;

}
