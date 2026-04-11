package nl.fontys.newswebapplication.services.exceptions.account.journalist;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JournalistNotFoundException extends ResponseStatusException {
    public JournalistNotFoundException() {
        super(HttpStatus.NOT_FOUND, "JOURNALIST_NOT_FOUND");
    }

    public JournalistNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}
