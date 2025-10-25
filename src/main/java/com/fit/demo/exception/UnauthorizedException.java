// java
package com.fit.demo.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class UnauthorizedException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public UnauthorizedException() {
        super("UnauthorizedException: Credenciales inválidas");
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Credenciales inválidas");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(401);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}