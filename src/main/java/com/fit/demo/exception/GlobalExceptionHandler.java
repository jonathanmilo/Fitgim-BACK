
package com.fit.demo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ProblemDetail> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return new ResponseEntity<>(ex.getBody(), ex.getStatusCode());
    }

    @ExceptionHandler(ErrorCreacionException.class)
    public ResponseEntity<ProblemDetail> manejarErrorCreacion(ErrorCreacionException ex) {
        return new ResponseEntity<>(ex.getBody(), ex.getStatusCode());
    }

    @ExceptionHandler(InvalidObjectIdException.class)
    public ResponseEntity<ProblemDetail> handleInvalidObjectId(InvalidObjectIdException ex) {
        return new ResponseEntity<>(ex.getBody(), ex.getStatusCode());
    }



    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(ValidationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

}
