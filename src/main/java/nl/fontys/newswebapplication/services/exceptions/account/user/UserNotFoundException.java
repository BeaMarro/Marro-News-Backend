package nl.fontys.newswebapplication.services.exceptions.account.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    public UserNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}
