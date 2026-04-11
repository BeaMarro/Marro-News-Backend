package nl.fontys.newswebapplication.services.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountNotFoundException extends ResponseStatusException {
    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND");
    }

    public AccountNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}
