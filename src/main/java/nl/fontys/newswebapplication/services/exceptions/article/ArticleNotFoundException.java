package nl.fontys.newswebapplication.services.exceptions.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticleNotFoundException extends ResponseStatusException {
    public ArticleNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ARTICLE_NOT_FOUND");
    }

    public ArticleNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}