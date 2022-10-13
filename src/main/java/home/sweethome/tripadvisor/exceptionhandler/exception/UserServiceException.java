package home.sweethome.tripadvisor.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserServiceException extends AbstractException {

    public UserServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }
}
