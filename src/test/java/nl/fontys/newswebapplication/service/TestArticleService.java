package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.exceptions.account.AccountExistsException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.journalist.JournalistNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleExistsException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticlesNotFoundException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import nl.fontys.newswebapplication.services.validation.ArticlesListValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestArticleService {
    @Mock
    private ArticleRepository articleRepositoryMock;
    @Mock
    private AccountRepository accountRepositoryMock;
    @Mock
    private AccountConverter accountConverter;
    @Mock
    private ArticleConverter articleConverter;
    @Mock
    private AccountValidation accountValidation;
    @Mock
    private ArticleValidation articleValidation;
    @Mock
    private ArticlesListValidation articlesListValidation;
    @InjectMocks
    private ArticleService articleService;

    private ArticleEntity articleEntity1;
    private ArticleEntity articleEntity2;
    private ArticleEntity invalidArticle;
    private ArticleEntity updatedArticleEntity;
    private Article article1;
    private Article article2;
    private Article updatedArticle;
    private ArticleRequest articleRequest;
    private ArticleRequest updatedArticleRequest;
    private ArticleRequest invalidArticleRequest;
    private final int approvedStatus = 2;

    private JournalistEntity journalistEntity;
    private JournalistResponse journalist;
    private AdminEntity adminEntity;
    private AccountEntity accountEntity;

    private Account account;

    @BeforeEach
    void setUp() {
        byte[] coverImage = new byte[] { 105, 109, 97, 103, 101, 46, 106, 112, 103 };
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Setup Journalist Entity
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(2);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePicture);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        // Setup Admin Entity
        adminEntity = new AdminEntity();
        adminEntity.setId(3);
        adminEntity.setFullName("Jane Doe");
        adminEntity.setUsername("Jane.Doe");
        adminEntity.setEmail("jane.doe@news.nl");
        adminEntity.setDateOfBirth(LocalDate.of(1995, 6, 10));
        adminEntity.setProfilePicture(profilePicture);
        adminEntity.setPassword("Admin123123");
        adminEntity.setCompany("Fontys");

        // Setup Account Entity
        accountEntity = AccountEntity.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture(profilePicture)
                .password("12345678910")
                .build();

        // Setup Article Entities

        int approvalStatus = 0;
        articleEntity1 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(approvalStatus)
                .build();

        articleEntity2 = ArticleEntity.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(1)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatus)
                .build();

        updatedArticleEntity = ArticleEntity.builder()
                .id(1)
                .heading("Updated Heading")
                .text("Updated Text")
                .authorEntity(journalistEntity)
                .genreId(Genre.TRAVEL.ordinal())
                .publishDate(LocalDate.now())
                .videoLink(null)
                .coverImage("image.png".getBytes())
                .build();

        invalidArticle = ArticleEntity.builder()
                .id(999)
                .build();

        // Setup Journalist object

        journalist = JournalistResponse.builder()
                .id(2)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture("john_doe.jpg")
                .department(Department.COPYRIGHTING)
                .build();

        // Setup Article objects

        article1 = Article.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .author(journalist)
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.empty())
                .coverImage("image.jpg")
                .status(ApprovalStatus.PENDING)
                .build();

        article2 = Article.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(LocalDate.now())
                .author(journalist)
                .genre(Genre.POLITICS)
                .video(Optional.of("www.video.nl"))
                .coverImage("hello.jpg")
                .status(ApprovalStatus.APPROVED)
                .build();

        articleRequest = ArticleRequest.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .authorId(journalist.getId())
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.empty())
                .coverImage("image.jpg")
                .build();

        updatedArticleRequest = ArticleRequest.builder()
                .id(1)
                .heading("Updated Heading")
                .text("Updated Text")
                .authorId(2)
                .genre(Genre.TRAVEL)
                .video(Optional.empty())
                .coverImage("image.png")
                .build();

        invalidArticleRequest = ArticleRequest.builder()
                .id(999)
                .build();

        updatedArticle = Article.builder()
                .id(1)
                .heading("Updated Heading")
                .text("Updated Text")
                .author(journalist)
                .genre(Genre.TRAVEL)
                .video(Optional.empty())
                .coverImage("image.png")
                .publishDate(LocalDate.of(2023, 11, 18))
                .status(ApprovalStatus.PENDING)
                .build();

        account = Account.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture("jake_doe.jpg")
                .password("12345678910")
                .build();
    }

    @Test
    void getArticles_shouldReturnAllApprovedArticlesConverted() {
        // Arrange
        when(articleRepositoryMock.getArticleEntitiesByApprovalStatusId(approvedStatus)).thenReturn(List.of(articleEntity1, articleEntity2));

        // Act
        List<Article> actualResult = articleService.getAll();
        List<Article> expectedResult = List.of(article1, article2);

        // Assert
        assertEquals(expectedResult.size(), actualResult.size());

        verify(articleRepositoryMock).getArticleEntitiesByApprovalStatusId(approvedStatus);
    }

    @Test
    void getArticlesByGenre_shouldReturnAllArticlesByGenreConverted() {
        // Arrange
        Genre genre = Genre.POLITICS;
        int genreId = genre.ordinal();

        when(articleRepositoryMock.getArticleEntityById(articleEntity2.getId())).thenReturn(articleEntity2);
        when(articleRepositoryMock.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus)).thenReturn(List.of(articleEntity2));

        // Act
        List<Article> actualResult = articleService.getByGenre(genre);
        List<Article> expectedResult = List.of(article2);

        // Assert
        assertEquals(expectedResult.size(), actualResult.size());

        verify(articleRepositoryMock).getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus);
    }

    @Test
    void testGetByEmptyGenre_shouldThrowException() {
        // Arrange
        Genre genre = Genre.BUSINESS_ECONOMICS;
        int genreId = genre.ordinal();
        List<ArticleEntity> emptyList = List.of();

        when(articleRepositoryMock.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus)).thenReturn(emptyList);
        doThrow(new ArticlesNotFoundException()).when(articlesListValidation).isEmpty(emptyList);

        // Act and Assert
        assertThrows(ArticlesNotFoundException.class, () -> {
            articleService.getByGenre(genre);
        });

        verify(articleRepositoryMock).getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus);
    }

    @Test
    void testGetByGenreReturningNullArticle_shouldThrowException() {
        Genre genre = Genre.TRAVEL;
        int genreId = genre.ordinal();
        int invalidArticleId = 999;
        List<ArticleEntity> invalidList = List.of(invalidArticle);

        when(articleRepositoryMock.getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus)).thenReturn(invalidList);
        when(articleRepositoryMock.getArticleEntityById(invalidArticleId)).thenReturn(invalidArticle);
        doThrow(new ArticlesNotFoundException()).when(articleValidation).isEmpty(invalidArticle);

        // Act and Assert
        assertThrows(ArticlesNotFoundException.class, () -> {
            articleService.getByGenre(genre);
        });

        verify(articleRepositoryMock).getArticleEntitiesByGenreIdAndApprovalStatusId(genreId, approvedStatus);
    }

    @Test
    void testGetAllArticlesByJournalist_shouldReturnAllArticlesByJournalistConverted() {
        // Arrange
        int journalistId = journalist.getId();

        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        when(articleRepositoryMock.getArticleEntitiesByAuthorEntity(journalistEntity)).thenReturn(List.of(articleEntity1, articleEntity2));

        // Act
        List<Article> actualResult = articleService.getByJournalist(journalistId);
        List<Article> expectedResult = List.of(article1, article2);

        // Assert
        assertEquals(expectedResult.size(), actualResult.size());

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
        verify(articleRepositoryMock).getArticleEntitiesByAuthorEntity(journalistEntity);
    }

    @Test
    void testGetAllArticlesByNonJournalist_shouldThrowException() {
        // Arrange
        int accountId = account.getId();

        when(accountRepositoryMock.getAccountEntityById(accountId)).thenReturn(accountEntity);
        doThrow(new JournalistNotFoundException()).when(accountValidation).isJournalistValid(accountEntity);

        // Act and Assert
        assertThrows(JournalistNotFoundException.class, () -> {
            articleService.getByJournalist(accountId);
        });

        verify(accountRepositoryMock).getAccountEntityById(accountId);
    }

    @Test
    void testGetAllArticlesByInvalidJournalist_shouldThrowException() {
        // Arrange
        int invalidJournalistId = 999;

        when(accountRepositoryMock.getAccountEntityById(invalidJournalistId)).thenReturn(accountEntity);
        doThrow(new JournalistNotFoundException()).when(accountValidation).isJournalistValid(accountEntity);

        // Act and Assert
        assertThrows(JournalistNotFoundException.class, () -> {
            articleService.getByJournalist(invalidJournalistId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidJournalistId);
    }

    @Test
    void testGetAllNonExistingArticlesByJournalist_shouldThrowException() {
        // Arrange
        int journalistId = journalist.getId();
        List<ArticleEntity> emptyList = List.of();

        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        when(articleRepositoryMock.getArticleEntitiesByAuthorEntity(journalistEntity)).thenReturn(emptyList);
        doThrow(new ArticlesNotFoundException()).when(articlesListValidation).isEmpty(emptyList);

        // Act and Assert
        assertThrows(ArticlesNotFoundException.class, () -> {
            articleService.getByJournalist(journalistId);
        });

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
        verify(articleRepositoryMock).getArticleEntitiesByAuthorEntity(journalistEntity);
    }

    @Test
    void testGetApprovedArticlesByJournalist_shouldReturnApprovedArticlesByJournalistConverted() {
        // Arrange
        int journalistId = journalist.getId();

        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        when(articleRepositoryMock.getArticleEntitiesByAuthorEntityAndApprovalStatusId(journalistEntity, approvedStatus)).thenReturn(List.of(articleEntity1, articleEntity2));

        // Act
        List<Article> actualResult = articleService.getApprovedArticlesByJournalist(journalistId);
        List<Article> expectedResult = List.of(article1, article2);

        // Assert
        assertEquals(expectedResult.size(), actualResult.size());

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
        verify(articleRepositoryMock).getArticleEntitiesByAuthorEntityAndApprovalStatusId(journalistEntity, approvedStatus);
    }

    @Test
    void testGetApprovedArticlesByInvalidJournalist_shouldThrowException() {
        // Arrange
        int invalidJournalistId = 999;

        when(accountRepositoryMock.getAccountEntityById(invalidJournalistId)).thenReturn(accountEntity);
        doThrow(new JournalistNotFoundException()).when(accountValidation).isJournalistValid(accountEntity);

        // Act and Assert
        assertThrows(JournalistNotFoundException.class, () -> {
            articleService.getApprovedArticlesByJournalist(invalidJournalistId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidJournalistId);
    }

    @Test
    void testGetArticleByValidId_shouldReturnArticleById() {
        // Arrange
        int articleId = 1;

        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);

        // Act
        Article actualResult = articleService.getById(articleId);

        // Assert
        assertEquals(article1.getId(), actualResult.getId());

        verify(articleRepositoryMock).getArticleEntityById(articleId);
    }

    @Test
    void testSaveNewArticle_shouldReturnArticleConverted() {
        // Arrange
        int journalistId = journalist.getId();

        // Act
        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        when(articleRepositoryMock.save(articleEntity1)).thenReturn(articleEntity1);

        Article expected = article1;
        Article actual = articleService.save(articleRequest);

        // Assert
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getHeading(), actual.getHeading());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getGenre(), actual.getGenre());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
        assertEquals(expected.getPublishDate(), actual.getPublishDate());
        assertEquals(expected.getVideo(), actual.getVideo());
        assertEquals(expected.getStatus(), actual.getStatus());

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
        verify(articleRepositoryMock).save(articleEntity1);
    }

    @Test
    void testSaveExistingArticle_shouldThrowException() {
        // Act
        int journalistId = journalist.getId();
        String heading = "Heading 1";

        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        doThrow(new ArticleExistsException()).when(articleValidation).existsByHeading(heading);

        // Act and Assert
        assertThrows(ArticleExistsException.class, () -> {
            articleService.save(articleRequest);
        });

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
    }

    @Test
    void testSaveArticleWithNonExistingJournalist_shouldThrowException() {
        // Act
        int invalidJournalistId = 2;
        JournalistEntity invalidJournalist = new JournalistEntity();
        invalidJournalist.setId(invalidJournalistId);

        when(accountRepositoryMock.getAccountEntityById(invalidJournalistId)).thenReturn(invalidJournalist);
        doThrow(new AccountNotFoundException()).when(accountValidation).isJournalistValid(invalidJournalist);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            articleService.save(articleRequest);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidJournalistId);
    }

    @Test
    void testSaveArticleWithInvalidAccountTypeSetAsJournalist_shouldThrowException() {
        // Act
        int invalidAccountId =  2;
        when(accountRepositoryMock.getAccountEntityById(invalidAccountId)).thenReturn(adminEntity);
        doThrow(new JournalistNotFoundException()).when(accountValidation).isJournalistValid(adminEntity);

        // Act and Assert
        assertThrows(JournalistNotFoundException.class, () -> {
            articleService.save(articleRequest);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidAccountId);
    }

    @Test
    void testSaveArticleWithNullVideo_shouldReturnArticleConverted() {
        // Arrange
        int journalistId = journalist.getId();
        articleEntity1.setVideoLink(null);

        // Act
        when(accountRepositoryMock.getAccountEntityById(journalistId)).thenReturn(journalistEntity);
        when(articleRepositoryMock.save(articleEntity1)).thenReturn(articleEntity1);

        Article expected = article1;
        Article actual = articleService.save(articleRequest);

        // Assert
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getHeading(), actual.getHeading());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getGenre(), actual.getGenre());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
        assertEquals(expected.getPublishDate(), actual.getPublishDate());
        assertEquals(expected.getVideo(), actual.getVideo());
        assertEquals(expected.getStatus(), actual.getStatus());

        verify(accountRepositoryMock).getAccountEntityById(journalistId);
        verify(articleRepositoryMock).save(articleEntity1);
    }

    @Test
    void testDeleteArticleByValidId_shouldReturnNothing() {
        // Arrange
        int id = article1.getId();

        // Act
        when(articleRepositoryMock.existsById(id)).thenReturn(true);
        articleRepositoryMock.deleteById(id);

        articleService.delete(id);

        // Assert
        verify(articleRepositoryMock, times(2)).deleteById(id);
    }

    @Test
    void testDeleteArticleByInvalidId_shouldThrowException() {
        // Arrange
        int id = 999;

        // Act and Assert
        when(articleRepositoryMock.existsById(id)).thenReturn(false);
        assertThrows(ArticleNotFoundException.class, () -> {
            articleService.delete(id);
        });

        // Assert
        verify(articleRepositoryMock).existsById(id);
    }

    @Test
    void testUpdateValidArticle_shouldReturnNothing() {
        // Arrange
        Article expectedResult = updatedArticle;

        // Act
        when(accountRepositoryMock.getAccountEntityById(journalistEntity.getId())).thenReturn(journalistEntity);
        when(articleRepositoryMock.getArticleEntityById(articleEntity1.getId())).thenReturn(articleEntity1);
        when(articleRepositoryMock.getArticleEntityById(updatedArticle.getId())).thenReturn(updatedArticleEntity);
        when(articleRepositoryMock.save(updatedArticleEntity)).thenReturn(updatedArticleEntity);

        articleService.update(updatedArticleRequest); // Update
        Article actualResult = articleService.getById(updatedArticleRequest.getId());

        // Assert
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getHeading(), actualResult.getHeading());
        assertEquals(expectedResult.getText(), actualResult.getText());
        assertEquals(expectedResult.getAuthor().getId(), actualResult.getAuthor().getId());
        assertEquals(LocalDate.now(), actualResult.getPublishDate()); // When updated -> Article publish date is set to now
        assertEquals(expectedResult.getGenre(), actualResult.getGenre());
        assertEquals(expectedResult.getVideo(), actualResult.getVideo());

        verify(accountRepositoryMock, times(1)).getAccountEntityById(journalistEntity.getId());
        verify(articleRepositoryMock, times(1)).getArticleEntityById(updatedArticle.getId());
        verify(articleRepositoryMock, times(1)).save(updatedArticleEntity);
    }

    @Test
    void testUpdateValidArticleWhereVideoIsNull_shouldReturnNothing() {
        // Arrange
        Article expectedResult = updatedArticle;
        updatedArticle.setVideo(Optional.empty());
        updatedArticleEntity.setVideoLink(null);

        // Act
        when(accountRepositoryMock.getAccountEntityById(journalistEntity.getId())).thenReturn(journalistEntity);
        when(articleRepositoryMock.getArticleEntityById(articleEntity1.getId())).thenReturn(articleEntity1);
        when(articleRepositoryMock.getArticleEntityById(updatedArticle.getId())).thenReturn(updatedArticleEntity);
        when(articleRepositoryMock.save(updatedArticleEntity)).thenReturn(updatedArticleEntity);

        articleService.update(updatedArticleRequest); // Update
        Article actualResult = articleService.getById(updatedArticleRequest.getId());

        // Assert
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getHeading(), actualResult.getHeading());
        assertEquals(expectedResult.getText(), actualResult.getText());
        assertEquals(expectedResult.getAuthor().getId(), actualResult.getAuthor().getId());
        assertEquals(LocalDate.now(), actualResult.getPublishDate()); // When updated -> Article publish date is set to now
        assertEquals(expectedResult.getGenre(), actualResult.getGenre());
        assertEquals(expectedResult.getVideo(), actualResult.getVideo());

        verify(accountRepositoryMock, times(1)).getAccountEntityById(journalistEntity.getId());
        verify(articleRepositoryMock, times(1)).getArticleEntityById(updatedArticle.getId());
        verify(articleRepositoryMock, times(1)).save(updatedArticleEntity);
    }

    @Test
    void testUpdateNonExistingArticle_shouldThrowException() {
        doThrow(new ArticleNotFoundException()).when(articleValidation).isEmpty(invalidArticleRequest);

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleService.update(invalidArticleRequest);
        });
    }
}
