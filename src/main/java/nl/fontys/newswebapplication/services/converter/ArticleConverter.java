package nl.fontys.newswebapplication.services.converter;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public final class ArticleConverter {
    public static Article convertEntityToArticle(ArticleEntity articleEntity) {
        AccountResponse account = AccountConverter.convertToAccountResponse(articleEntity.getAuthorEntity());

        Article article = Article.builder()
                .id(articleEntity.getId())
                .heading(articleEntity.getHeading())
                .text(articleEntity.getText())
                .publishDate(articleEntity.getPublishDate())
                .author((JournalistResponse) account)
                .genre(Genre.values()[articleEntity.getGenreId()])
                .status(ApprovalStatus.values()[articleEntity.getApprovalStatusId()])
                .build();

        if(articleEntity.getCoverImage() != null) {
            String coverImage = new String(articleEntity.getCoverImage());
            article.setCoverImage(coverImage);
        }

        if (articleEntity.getVideoLink() != null) {
            article.setVideo(Optional.of(articleEntity.getVideoLink()));
        } else {
            article.setVideo(Optional.empty());
        }

        return article;
    }

    public static ArticleEntity convertRequestToEntity(ArticleRequest articleRequest, AccountEntity journalistEntity) {
        ArticleEntity articleEntity = ArticleEntity.builder()
                .id(articleRequest.getId())
                .heading(articleRequest.getHeading())
                .text(articleRequest.getText())
                .publishDate(LocalDate.now())
                .authorEntity((JournalistEntity) journalistEntity)
                .genreId(articleRequest.getGenre().ordinal())
                .coverImage(articleRequest.getCoverImage().getBytes())
                .approvalStatusId(0) // Set Status to Pending
                .build();

        if (articleRequest.getVideo().isPresent()) {
            articleEntity.setVideoLink(articleRequest.getVideo().get());
        } else {
            articleEntity.setVideoLink(null);
        }

        return articleEntity;
    }
}
