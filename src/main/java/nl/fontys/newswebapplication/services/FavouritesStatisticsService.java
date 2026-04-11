package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class FavouritesStatisticsService {
    private final AccountRepository accountRepository;
    private final FavouritesListRepository favouritesListRepository;

    public Map<String, Integer> getTotalArticlesPerGenre(int accountId) {
        Map<String, Integer> totalArticlesPerGenre = new HashMap<>();

        UserEntity userEntity = null;
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if(accountEntity.isPresent()) {
            userEntity = (UserEntity) accountEntity.get();
        }

        for (Genre genre : Genre.values()) {
            String genreName = genre.name();
            int totalArticles = favouritesListRepository.countTotalArticlesByGenreAndUser(userEntity, genre.ordinal());
            totalArticlesPerGenre.put(genreName, totalArticles);
        }

        return totalArticlesPerGenre;
    }
}
