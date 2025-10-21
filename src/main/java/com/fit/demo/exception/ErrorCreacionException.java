
package com.fit.demo.exception;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class ErrorCreacionException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public ErrorCreacionException(String mensaje) {
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), mensaje);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(400);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}
