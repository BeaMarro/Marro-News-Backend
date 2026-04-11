package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.ReadingTimeService;
import nl.fontys.newswebapplication.services.exceptions.account.user.UserNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.login.InvalidCredentialsException;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestReadingTimeService {
    @Mock
    private ArticleRepository articleRepositoryMock;
    @Mock
    private ArticleValidation articleValidation;
    @InjectMocks
    private ReadingTimeService readingTimeService;

    private ArticleEntity articleEntity1;
    private ArticleEntity articleEntity2;
    private ArticleEntity invalidArticle;

    @BeforeEach
    void setup() {
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] profilePicture = "profile_picture.jpg".getBytes();
        int approvalStatus = 2;

        // Setup Journalist Entity
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePicture);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        articleEntity1 = ArticleEntity.builder()
                .id(1)
                .heading("Formula 1: 'Most need 150km to get used to F1, Verstappen needed a lap'")
                .text("Formula 1".repeat(2000))
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(approvalStatus)
                .build();

        articleEntity2 = ArticleEntity.builder()
                .id(2)
                .heading("Formula 1: 'Most need 150km to get used to F1, Verstappen needed a lap'")
                .text("Article Test 1234")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(approvalStatus)
                .build();

        invalidArticle = null;
    }

    @Test
    void testGetValidArticleReadingTime_shouldReturnReadingTime() {
        // Arrange
        int id = articleEntity1.getId();
        int expected = 10;

        when(articleRepositoryMock.getArticleEntityById(id)).thenReturn(articleEntity1);

        // Act
        int actual = readingTimeService.getReadingTimeByArticleId(id);

        // Assert
        assertEquals(expected, actual);

        verify(articleRepositoryMock).getArticleEntityById(id);
    }

    @Test
    void testGetValidArticleReadingTimeWhereReadingTimeLessThan1_shouldReturnReadingTimeAs1() {
        // Arrange
        int id = articleEntity2.getId();
        int expected = 1;

        when(articleRepositoryMock.getArticleEntityById(id)).thenReturn(articleEntity2);

        // Act
        int actual = readingTimeService.getReadingTimeByArticleId(id);

        // Assert
        assertEquals(expected, actual);

        verify(articleRepositoryMock).getArticleEntityById(id);
    }

    @Test
    void testGetReadingTimeByInvalidArticle_shouldThrowException() {
        // Arrange
        int invalidId = 999;

        doThrow(new ArticleNotFoundException()).when(articleValidation).isEmpty(invalidArticle);
        when(articleRepositoryMock.getArticleEntityById(invalidId)).thenReturn(invalidArticle);

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            readingTimeService.getReadingTimeByArticleId(invalidId);
        });

        verify(articleValidation).isEmpty(invalidArticle);
        verify(articleRepositoryMock).getArticleEntityById(invalidId);
    }
}
