package home.sweethome.tripadvisor.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserServiceException extends RuntimeException {
    private final HttpStatus status;

    public UserServiceException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }
}
