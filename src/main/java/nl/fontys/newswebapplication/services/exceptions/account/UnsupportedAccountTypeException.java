package nl.fontys.newswebapplication.services.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnsupportedAccountTypeException extends ResponseStatusException {
    public UnsupportedAccountTypeException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "UNSUPPORTED_ACCOUNT_TYPE");
    }

    public UnsupportedAccountTypeException(String errorCode) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode);
    }
}
