package nl.fontys.newswebapplication.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.*;
import nl.fontys.newswebapplication.repositories.*;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.FavouriteArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import nl.fontys.newswebapplication.services.validation.FavouritesListValidation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FavoritesListService {
    private final AccountRepository accountRepository;
    private final FavouritesListRepository favouritesListEntityRepository;
    private final ArticleRepository articleRepository;
    private final AccountValidation accountValidation;
    private final ArticleValidation articleValidation;
    private final FavouritesListValidation favouritesListValidation;

    public List<Article> getById(int userId) { // Retrieve a user's favorite articles from the favoriteArticleRepository
        AccountEntity account = accountRepository.getAccountEntityById(userId);

        // User Validation
        accountValidation.isUserValid(account);

        UserEntity user = (UserEntity) accountRepository.getAccountEntityById(userId);
        List<FavouriteArticleEntity> favoriteArticles = favouritesListEntityRepository.findByUser(user);
        List<Article> favoritesList = new ArrayList<>();

        for (FavouriteArticleEntity favoriteArticle : favoriteArticles) {
            Article article = ArticleConverter.convertEntityToArticle(articleRepository.getArticleEntityById(favoriteArticle.getArticle().getId()));
            favoritesList.add(article);
        }

        return favoritesList;
    }

    @Transactional
    public List<Article> save(int userId, int articleId) {
        AccountEntity account = accountRepository.getAccountEntityById(userId);

        // User Validation
        accountValidation.isUserValid(account);

        UserEntity user = (UserEntity) account;
        ArticleEntity article = articleRepository.getArticleEntityById(articleId);
        // Article Validation
        validateArticle(article);

        // Articles List Validation
        validateFavouritesList(user, article);

        FavouriteArticleEntity favoriteArticle = new FavouriteArticleEntity();
        favoriteArticle.setUser(user);
        favoriteArticle.setArticle(article);
        favouritesListEntityRepository.save(favoriteArticle);

        return getById(userId);
    }

    @Transactional
    public void delete(int userId, int articleId) {
        AccountEntity account = accountRepository.getAccountEntityById(userId);
        ArticleEntity article = articleRepository.getArticleEntityById(articleId);

        // User and Article Validation
        accountValidation.isUserValid(account);
        validateArticle(article);
        UserEntity user = (UserEntity) account;

        FavouriteArticleEntity favouriteArticleEntity = favouritesListEntityRepository.findByUserAndArticle(user, article);

        favouritesListEntityRepository.delete(favouriteArticleEntity);
    }

    private void validateFavouritesList(UserEntity user, ArticleEntity article) {
        // Check if the user and article exist and if the article is not already in the user's favorites
        favouritesListValidation.exists(article, user);
    }

    private void validateArticle(ArticleEntity article) {
        articleValidation.isEmpty(article);
    }
}
