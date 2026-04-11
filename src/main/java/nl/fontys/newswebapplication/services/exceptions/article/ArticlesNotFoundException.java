package nl.fontys.newswebapplication.services.exceptions.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticlesNotFoundException extends ResponseStatusException {
    public ArticlesNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ARTICLE_NOT_FOUND");
    }

    public ArticlesNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}