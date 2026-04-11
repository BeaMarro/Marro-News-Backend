package nl.fontys.newswebapplication.services.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountExistsException extends ResponseStatusException {
    public AccountExistsException(String errorCode) {
        super(HttpStatus.CONFLICT, errorCode);
    }
}
