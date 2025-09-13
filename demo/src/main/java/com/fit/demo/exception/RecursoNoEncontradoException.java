
package com.fit.demo.exception;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class RecursoNoEncontradoException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public RecursoNoEncontradoException() {
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Recurso no encontrado");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(404);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}