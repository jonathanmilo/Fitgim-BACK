package com.fit.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getBody());
    }

    @ExceptionHandler(DisabledUserException.class)
    public ResponseEntity<ProblemDetail> handleDisabled(DisabledUserException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getBody());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatus(ResponseStatusException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason() != null ? ex.getReason() : ex.getMessage());
        return new ResponseEntity<>(pd, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAny(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

}