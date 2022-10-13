package home.sweethome.tripadvisor.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TripServiceException extends AbstractException {

    public TripServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }
}
