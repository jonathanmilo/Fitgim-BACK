package com.fit.demo.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class DisabledUserException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public DisabledUserException() {
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Cuenta no habilitada");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(403);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}