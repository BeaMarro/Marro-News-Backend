package nl.fontys.newswebapplication.services.exceptions.field;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidFieldFormatException extends ResponseStatusException {
    public InvalidFieldFormatException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}