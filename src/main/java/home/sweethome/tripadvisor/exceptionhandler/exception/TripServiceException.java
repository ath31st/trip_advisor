package home.sweethome.tripadvisor.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TripServiceException extends RuntimeException {
    private final HttpStatus status;

    public TripServiceException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }
}
