package nl.fontys.newswebapplication.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.*;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.repositories.*;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import nl.fontys.newswebapplication.services.validation.ArticlesListValidation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class ArticleService {
    private final AccountRepository accountRepository;
    private final ArticleRepository articleRepository;
    private final ArticleValidation articleValidation;
    private final AccountValidation accountValidation;
    private final ArticlesListValidation articlesListValidation;

    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();

        for(ArticleEntity articleEntity : articleRepository.getArticleEntitiesByApprovalStatusId(2)) { // Checks if the article has been approved - Only approved articles should be returned (2 - APPROVED)
            Article article = ArticleConverter.convertEntityToArticle(articleEntity);
            articles.add(article);
        }

        return articles;
    }

    public Article getById(int id) {
        ArticleEntity articleEntity = this.articleRepository.getArticleEntityById(id);

        articleValidation.isEmpty(articleEntity); // Checks if the article was found

        return ArticleConverter.convertEntityToArticle(articleEntity);
    }

    public List<Article> getByGenre(Genre genre)
    {
        List<Article> articles = new ArrayList<>();

        int statusId = 2; // Status = 2 -> Approved
        int genreId = genre.ordinal();
        List<ArticleEntity> articleEntities = articleRepository.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, statusId);

        // Validate articles list
        validateArticlesList(articleEntities);

        for(ArticleEntity articleEntity : articleEntities) {
            articles.add(getById(articleEntity.getId()));
        }

        return articles;
    }

    @Transactional
    public Article save(ArticleRequest articleRequest) {
        // Get Journalist
        AccountEntity accountEntity = accountRepository.getAccountEntityById(articleRequest.getAuthorId());

        // Validate Article
        articleValidation.existsByHeading(articleRequest.getHeading());
        validateArticle(articleRequest);
        accountValidation.isJournalistValid(accountEntity);

        ArticleEntity articleEntity = ArticleConverter.convertRequestToEntity(articleRequest, accountEntity);

        // Save and return article
        ArticleEntity articleResponse = articleRepository.save(articleEntity);
        return ArticleConverter.convertEntityToArticle(articleResponse);
    }

    @Transactional
    public void delete(int id) {
        if(articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
        } else {
            throw new ArticleNotFoundException("Article with id " + id + " not found");
        }
    }

    @Transactional
    public void update(ArticleRequest articleRequest) {
        // Validate ArticleRequest
        validateArticle(articleRequest);

        // Reconstruct Journalist from ArticleRequest
        AccountEntity authorEntity = accountRepository.getAccountEntityById(articleRequest.getAuthorId());

        JournalistEntity journalistEntity = (JournalistEntity) authorEntity;
        ArticleEntity updatedArticleEntity = ArticleConverter.convertRequestToEntity(articleRequest, journalistEntity);

        // Save updated version to the database
        articleRepository.save(updatedArticleEntity);
    }

    public List<Article> getByJournalist(int journalistId) {
        return getArticlesByJournalistAndStatus(journalistId, -1); // -1 to get all articles regardless of approval status
    }

    public List<Article> getApprovedArticlesByJournalist(int journalistId) {
        return getArticlesByJournalistAndStatus(journalistId, 2); // Status = 2 -> Approved
    }

    private List<Article> getArticlesByJournalistAndStatus(int journalistId, int approvalStatusId) {
        AccountEntity journalistEntity = accountRepository.getAccountEntityById(journalistId);

        // Journalist validation
        accountValidation.isJournalistValid(journalistEntity);
        JournalistEntity journalist = (JournalistEntity) journalistEntity;

        List<ArticleEntity> articleEntities;

        if (approvalStatusId == -1) {
            articleEntities = articleRepository.getArticleEntitiesByAuthorEntity(journalist);
        } else {
            articleEntities = articleRepository.getArticleEntitiesByAuthorEntityAndApprovalStatusId(journalist, approvalStatusId);
        }

        // Validate Articles List
        validateArticlesList(articleEntities);

        List<Article> articles = new ArrayList<>();

        for (ArticleEntity articleEntity : articleEntities) {
            articles.add(ArticleConverter.convertEntityToArticle(articleEntity));
        }

        return articles;
    }

    // Validation

    private void validateArticle(ArticleRequest article) {
        articleValidation.isEmpty(article);
        articleValidation.isHeadingLengthValid(article.getHeading());
        articleValidation.isTextLengthValid(article.getText());
        articleValidation.isGenreValid(article.getGenre().ordinal());
        articleValidation.isVideoLinkValid(article.getVideo());
        articleValidation.isCoverImageValid(article.getCoverImage());
    }

    private void validateArticlesList(List<ArticleEntity> articles) {
        articlesListValidation.isEmpty(articles);
    }
}
