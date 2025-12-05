package main.java.com.fit.demo.exception;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
public class RefreshInvalidoException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public RefreshInvalidoException() {
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Token de refresco inválido o expirado");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(402);
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
    
}
