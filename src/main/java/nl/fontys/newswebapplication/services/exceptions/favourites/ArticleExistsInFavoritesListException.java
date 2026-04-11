package nl.fontys.newswebapplication.services.exceptions.favourites;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticleExistsInFavoritesListException extends ResponseStatusException {
    public ArticleExistsInFavoritesListException() {
        super(HttpStatus.CONFLICT, "ARTICLE_EXISTS");
    }
}