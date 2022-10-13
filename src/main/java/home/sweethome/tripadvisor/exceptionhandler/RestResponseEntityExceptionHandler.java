package home.sweethome.tripadvisor.exceptionhandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import home.sweethome.tripadvisor.exceptionhandler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JWTVerificationException.class)
    protected ResponseEntity<Response> handleJwtException(JWTVerificationException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Response> handleValidException(ConstraintViolationException e) {
        Response response = Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PayloadPieceException.class)
    protected ResponseEntity<Response> handlePayloadException(PayloadPieceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(LoginCredentialException.class)
    protected ResponseEntity<Response> handleLoginCredentialException(LoginCredentialException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(TripServiceException.class)
    protected ResponseEntity<Response> handleTripServiceException(TripServiceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(UserServiceException.class)
    protected ResponseEntity<Response> handleUserServiceException(UserServiceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    private Response buildResponse(AbstractException e) {
        return Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
    }
}
