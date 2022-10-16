package dashboard.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(exception.getErrorCode())
                        .message(exception.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}
