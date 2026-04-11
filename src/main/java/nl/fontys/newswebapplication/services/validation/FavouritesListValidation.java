package nl.fontys.newswebapplication.services.validation;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.favourites.FavouriteArticleNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FavouritesListValidation {
    private FavouritesListRepository favouritesListRepository;

    public void exists(ArticleEntity articleEntity, UserEntity userEntity) {
        if(favouritesListRepository.existsByUserAndArticle(userEntity, articleEntity)) {
            throw new FavouriteArticleNotFoundException("Favourite article not found");
        }
    }
}
