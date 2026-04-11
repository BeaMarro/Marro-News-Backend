package nl.fontys.newswebapplication.services.exceptions.favourites;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FavouriteArticleNotFoundException extends ResponseStatusException {
    public FavouriteArticleNotFoundException(String errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}