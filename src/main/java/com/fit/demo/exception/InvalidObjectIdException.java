
package com.fit.demo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class InvalidObjectIdException extends IllegalArgumentException implements ErrorResponse {
    private final ProblemDetail body;

    public InvalidObjectIdException() {
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Invalid ObjectId string");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.valueOf(400);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}
