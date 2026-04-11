package nl.fontys.newswebapplication.services.exceptions.field;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MissingFieldException extends ResponseStatusException {
    public MissingFieldException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}