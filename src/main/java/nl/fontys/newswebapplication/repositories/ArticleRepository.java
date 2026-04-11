package nl.fontys.newswebapplication.repositories;

import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    ArticleEntity getArticleEntityById(int articleId);
    List<ArticleEntity> getArticleEntitiesByApprovalStatusId(int statusId);
    List<ArticleEntity> getArticleEntitiesByGenreIdAndApprovalStatusId(int genreId, int statusId);
    List<ArticleEntity> getArticleEntitiesByAuthorEntity(JournalistEntity author);
    List<ArticleEntity> getArticleEntitiesByAuthorEntityAndApprovalStatusId(JournalistEntity author, int statusId);
    boolean existsByHeading(String heading);
    @Query("SELECT COUNT(a) " +
            "FROM ArticleEntity a " +
            "WHERE a.authorEntity.id = :journalistId " +
            "GROUP BY a.authorEntity.id ")
    int countTotalArticlesByJournalist(@Param("journalistId") int journalistId);
    @Query("SELECT COUNT(a) * 100.0 / (SELECT COUNT(a2) FROM ArticleEntity a2) " +
            "FROM ArticleEntity a " +
            "WHERE a.authorEntity.id = :journalistId " +
            "GROUP BY a.authorEntity.id ")
    Double findArticleShareByJournalist(@Param("journalistId") int journalistId);
}
