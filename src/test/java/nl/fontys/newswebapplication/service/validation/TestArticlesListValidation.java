package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.exceptions.article.ArticlesNotFoundException;
import nl.fontys.newswebapplication.services.validation.ArticlesListValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestArticlesListValidation {
    private ArticlesListValidation articlesListValidation = new ArticlesListValidation();
    private List<ArticleEntity> articles = new ArrayList<>();

    @BeforeEach
    void setup() {
        int approvalStatusId = ApprovalStatus.APPROVED.ordinal();
        byte[] coverImage = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] profilePictureBlob = "profile_picture.jpg".getBytes();

        // Journalist
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setId(2);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePictureBlob);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        // Article
        ArticleEntity articleEntity1 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        ArticleEntity articleEntity2 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        articles.add(articleEntity1);
        articles.add(articleEntity2);
    }

    @Test
    void testIsFullArticlesListEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> articlesListValidation.isEmpty(articles));
    }

    @Test
    void testIsEmptyArticlesListEmpty_shouldThrowException() {
        // Arrange
        List<ArticleEntity> emptyArticles = null;
        // Act and Assert
        assertThrows(ArticlesNotFoundException.class, () -> {
            articlesListValidation.isEmpty(emptyArticles);
        });
    }
}
