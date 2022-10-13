package home.sweethome.tripadvisor.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class LoginCredentialException extends RuntimeException {

    private final HttpStatus status;

    public LoginCredentialException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }

}
