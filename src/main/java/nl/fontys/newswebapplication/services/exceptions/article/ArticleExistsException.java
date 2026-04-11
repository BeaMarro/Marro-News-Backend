package nl.fontys.newswebapplication.services.exceptions.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticleExistsException extends ResponseStatusException {
    public ArticleExistsException() {
        super(HttpStatus.BAD_REQUEST, "ARTICLE_EXISTS");
    }

    public ArticleExistsException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
