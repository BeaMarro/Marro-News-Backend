package nl.fontys.newswebapplication.repository;

import jakarta.transaction.Transactional;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class TestArticleRepository {
    @Mock
    private ArticleRepository articleRepository;

    private ArticleEntity article1;
    private ArticleEntity updatedArticle1;
    private ArticleEntity emptyArticle;
    private JournalistEntity journalist;
    private List<ArticleEntity> articles;
    private final int approvalStatusId = ApprovalStatus.APPROVED.ordinal();

    @BeforeEach
    void setUp() {
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] updatedCoverImage = new byte[]{ 11, 12, 13, 14, 15, 16, 17, 18, 19 };
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Setup Journalist Entity
        journalist = new JournalistEntity();
        journalist.setId(2);
        journalist.setFullName("John Doe");
        journalist.setUsername("J.Doe");
        journalist.setEmail("j.doe@news.com");
        journalist.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalist.setProfilePicture(profilePicture);
        journalist.setPassword("12345678910");
        journalist.setDepartmentId(2);


        // Setup Article Entities
        article1 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalist)
                .genreId(2)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        updatedArticle1 = ArticleEntity.builder()
                .id(1)
                .heading("Updated Heading 1")
                .text("Updated Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalist)
                .genreId(1)
                .videoLink("www.video_updated.nl")
                .coverImage(updatedCoverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        ArticleEntity article2 = ArticleEntity.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(LocalDate.now())
                .authorEntity(journalist)
                .genreId(1)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        emptyArticle = null;

        articles = List.of(article1, article2);
    }

    @Test
    void testGetAllArticles_shouldReturnAllArticles() {
        // Arrange
        when(articleRepository.findAll()).thenReturn(articles);

        // Act
        List<ArticleEntity> actual = articleRepository.findAll();

        // Assert
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void testExistsByExistingHeading_shouldReturnTrue() {
        // Arrange
        String heading = "Breaking News at Fontys University of Applied Science";
        when(articleRepository.existsByHeading(heading)).thenReturn(true);

        // Act
        boolean exists = articleRepository.existsByHeading(heading);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByNotExistingHeading_shouldReturnFalse() {
        // Arrange
        String heading = "Article does not exist";
        when(articleRepository.existsByHeading(heading)).thenReturn(false);

        // Act
        boolean exists = articleRepository.existsByHeading(heading);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testGetArticleById_shouldReturnArticle() {
        // Arrange
        int id = article1.getId();
        ArticleEntity expected = article1;
        when(articleRepository.getArticleEntityById(id)).thenReturn(article1);

        // Act
        ArticleEntity actual = articleRepository.getArticleEntityById(id);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetArticleByInvalidId_shouldReturnNull() {
        // Arrange
        int invalidArticleId = 999;
        when(articleRepository.getArticleEntityById(invalidArticleId)).thenReturn(null);

        // Act
        ArticleEntity actual = articleRepository.getArticleEntityById(invalidArticleId);

        // Assert
        assertNull(actual); // When an invalid article is retrieved, null is returned
    }

    @Test
    void testGetArticlesByGenre_shouldReturnAllArticles() {
        // Arrange
        int genreId = 1;
        when(articleRepository.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvalStatusId)).thenReturn(articles);

        // Act
        List<ArticleEntity> actual = articleRepository.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvalStatusId);

        // Assert
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void testGetArticlesByInvalidGenre_shouldReturnNull() {
        // Arrange
        int invalidGenreId = 999;
        when(articleRepository.getArticleEntitiesByGenreIdAndApprovalStatusId(invalidGenreId, approvalStatusId)).thenReturn(null);

        // Act
        List<ArticleEntity> actual = articleRepository.getArticleEntitiesByGenreIdAndApprovalStatusId(invalidGenreId, approvalStatusId);

        // Assert
        assertNull(actual);
    }

    @Test
    void testGetArticlesByJournalist_shouldReturnAllArticles() {
        // Arrange
        when(articleRepository.getArticleEntitiesByAuthorEntity(journalist)).thenReturn(articles);

        // Act
        List<ArticleEntity> actual = articleRepository.getArticleEntitiesByAuthorEntity(journalist);

        // Assert
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void testGetArticlesByInvalidJournalist_shouldReturnNull() {
        // Arrange
        when(articleRepository.getArticleEntitiesByAuthorEntity(journalist)).thenReturn(null);

        // Act
        List<ArticleEntity> actual = articleRepository.getArticleEntitiesByAuthorEntity(journalist);

        // Assert
        assertNull(actual);
    }

    @Test
    void testCreateValidArticle_shouldReturnArticle() {
        // Arrange
        ArticleEntity expected = article1;
        when(articleRepository.save(expected)).thenReturn(expected);

        // Act
        ArticleEntity actual = articleRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateEmptyArticle_shouldReturnNull() {
        // Arrange
        when(articleRepository.save(emptyArticle)).thenReturn(null);

        // Act
        ArticleEntity actual = articleRepository.save(emptyArticle);

        // Assert
        assertNull(actual);
    }

    @Test
    void testDeleteValidArticle_shouldReturnNothing() {
        // Arrange
        int articleId = article1.getId();
        int actual;
        int expected = articles.size() - 1;
        when(articleRepository.findAll()).thenReturn(List.of(article1));

        // Act
        articleRepository.deleteById(articleId);
        actual = articleRepository.findAll().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateValidArticle_shouldReturnArticle() {
        // Arrange
        ArticleEntity expected = updatedArticle1;
        when(articleRepository.save(expected)).thenReturn(expected);

        // Act
        ArticleEntity actual = articleRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateEmptyArticle_shouldReturnNull() {
        // Arrange
        when(articleRepository.save(emptyArticle)).thenReturn(null);

        // Act
        ArticleEntity actual = articleRepository.save(emptyArticle);

        // Assert
        assertNull(actual);
    }
}
