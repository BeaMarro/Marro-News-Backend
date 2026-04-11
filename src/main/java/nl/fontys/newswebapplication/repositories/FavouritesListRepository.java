package nl.fontys.newswebapplication.repositories;

import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.FavouriteArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouritesListRepository extends JpaRepository<FavouriteArticleEntity, Integer> {
    List<FavouriteArticleEntity> findByUser(UserEntity user);
    FavouriteArticleEntity findByUserAndArticle(UserEntity user, ArticleEntity articleEntity);
    boolean existsByUserAndArticle(UserEntity user, ArticleEntity article);
    @Query("SELECT COUNT(a) " +
            "FROM FavouriteArticleEntity f JOIN f.article a " +
            "WHERE f.user = :user AND a.genreId = :genreId")
    int countTotalArticlesByGenreAndUser(@Param("user") UserEntity user, @Param("genreId") int genreId);
}
