package nl.fontys.newswebapplication.services.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAccountTypeException extends ResponseStatusException {
    public InvalidAccountTypeException(String errorCode) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode);
    }
}
