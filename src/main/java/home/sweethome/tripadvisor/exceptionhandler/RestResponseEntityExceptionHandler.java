package home.sweethome.tripadvisor.exceptionhandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import home.sweethome.tripadvisor.exceptionhandler.exception.LoginCredentialException;
import home.sweethome.tripadvisor.exceptionhandler.exception.PayloadPieceException;
import home.sweethome.tripadvisor.exceptionhandler.exception.TripServiceException;
import home.sweethome.tripadvisor.exceptionhandler.exception.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PayloadPieceException.class)
    protected ResponseEntity<Response> handlePayloadException(PayloadPieceException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(JWTVerificationException.class)
    protected ResponseEntity<Response> handleJwtException(JWTVerificationException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginCredentialException.class)
    protected ResponseEntity<Response> handleLoginCredentialException(LoginCredentialException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(TripServiceException.class)
    protected ResponseEntity<Response> handleTripServiceException(TripServiceException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(UserServiceException.class)
    protected ResponseEntity<Response> handleUserServiceException(UserServiceException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
        return new ResponseEntity<>(response, e.getStatus());
    }
}
