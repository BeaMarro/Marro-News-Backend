package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.*;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.FavoritesListService;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.account.user.UserNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.favourites.ArticleExistsInFavoritesListException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import nl.fontys.newswebapplication.services.validation.FavouritesListValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestFavouritesListService {
    @Mock
    private FavouritesListRepository favouritesListRepositoryMock;
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
    private FavouritesListValidation favouritesListValidation;
    @InjectMocks
    private ArticleService articleService;
    @InjectMocks
    private FavoritesListService favoritesListService;

    private ArticleEntity articleEntity1;
    private ArticleEntity articleEntity2;
    private ArticleEntity invalidArticle;
    private Article article1;

    private FavouriteArticleEntity favouriteArticleEntity1;
    private FavouriteArticleEntity favouriteArticleEntity2;

    private JournalistEntity journalistEntity;
    private UserEntity userEntity;
    private AccountEntity invalidUser;

    @BeforeEach
    void setUp() {
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Setup Journalist Entity
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePicture);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        // Setup User Entity
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Jake Doe");
        userEntity.setUsername("Jake.Doe");
        userEntity.setEmail("jake.doe@gmail.com");
        userEntity.setDateOfBirth(LocalDate.of(1995, 7, 8));
        userEntity.setProfilePicture(profilePicture);
        userEntity.setPassword("Jake1234");
        userEntity.setBio("Test");

        invalidUser = AccountEntity.builder()
            .id(999)
            .build();

        // Setup Article Entities
        int approvalStatus = 2;
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
                .favoriteArticles(List.of(userEntity))
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
                .favoriteArticles(List.of(userEntity))
                .build();

        invalidArticle = ArticleEntity.builder()
                .id(999)
                .build();

        // Setup Journalist object

        JournalistResponse journalist = JournalistResponse.builder()
                .id(1)
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
                .status(ApprovalStatus.APPROVED)
                .build();

        // Favourite Article Entities
        favouriteArticleEntity1 = FavouriteArticleEntity.builder()
                .user(userEntity)
                .article(articleEntity1)
                .build();

        favouriteArticleEntity2 = FavouriteArticleEntity.builder()
                .user(userEntity)
                .article(articleEntity2)
                .build();
    }

    @Test
    void testGetFavouritesListByValidUser_shouldReturnFavouritesList() {
        // Arrange
        int userId = userEntity.getId();
        List<Article> expected = List.of(article1);

        // Mock the behavior of other dependencies
        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(1)).thenReturn(articleEntity1);
        when(favouritesListRepositoryMock.findByUser(userEntity)).thenReturn(List.of(favouriteArticleEntity1));

        // Act
        List<Article> actual = favoritesListService.getById(userId);

        // Assert
        assertEquals(expected.size(), actual.size());

        verify(accountRepositoryMock, times(2)).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(1);
        verify(favouritesListRepositoryMock).findByUser(userEntity);
    }

    @Test
    void testGetFavouritesListByNonExistingUser_shouldThrowException() {
        // Arrange
        int invalidUserId = 999;

        when(accountRepositoryMock.getAccountEntityById(invalidUserId)).thenReturn(invalidUser);
        doThrow(new AccountNotFoundException()).when(accountValidation).isUserValid(invalidUser);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            favoritesListService.getById(invalidUserId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidUserId);
    }

    @Test
    void testGetFavouritesListByInvalidAccountType_shouldThrowException() {
        // Arrange
        int invalidAccountId = 999;
        when(accountRepositoryMock.getAccountEntityById(invalidAccountId)).thenReturn(journalistEntity);
        doThrow(new UserNotFoundException()).when(accountValidation).isUserValid(journalistEntity);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            favoritesListService.getById(invalidAccountId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidAccountId);
    }

    @Test
    void testGetFavouritesListByInvalidUserType_shouldThrowException() {
        // Arrange
        int invalidUserTypeId = 99;

        when(accountRepositoryMock.getAccountEntityById(invalidUserTypeId)).thenThrow(new UnsupportedAccountTypeException("Unsupported account type"));

        // Act and Assert

        assertThrows(UnsupportedAccountTypeException.class, () -> {
            favoritesListService.getById(invalidUserTypeId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidUserTypeId);
    }

    @Test
    void testSaveValidArticleToValidUserFavouritesList_shouldReturnFavouritesList() {
        // Arrange
        int userId = userEntity.getId();
        int articleId = articleEntity1.getId();
        List<Article> expected = List.of(article1);

        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(1)).thenReturn(articleEntity1);
        when(articleRepositoryMock.getArticleEntityById(2)).thenReturn(articleEntity2);
        when(favouritesListRepositoryMock.findByUser(userEntity)).thenReturn(List.of(favouriteArticleEntity2));
        when(favouritesListRepositoryMock.save(favouriteArticleEntity1)).thenReturn(favouriteArticleEntity1);

        // Act
        List<Article> actual = favoritesListService.save(userId, articleId);

        // Assert
        assertEquals(expected.size(), actual.size());

        verify(accountRepositoryMock, times(3)).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(1);
        verify(articleRepositoryMock).getArticleEntityById(2);
        verify(favouritesListRepositoryMock).save(favouriteArticleEntity1);
    }


    @Test
    void testSaveNonExistingArticleToFavouritesList_shouldThrowException() {
        // Arrange
        int userId = userEntity.getId();
        int invalidArticleId = 999;

        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(invalidArticleId)).thenReturn(invalidArticle);
        doThrow(new ArticleNotFoundException()).when(articleValidation).isEmpty(invalidArticle);

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            favoritesListService.save(userId, invalidArticleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(invalidArticleId);
    }

    @Test
    void testSaveExistingArticleToFavouritesList_shouldThrowException() {
        // Arrange
        int userId = userEntity.getId();
        int articleId = articleEntity1.getId();

        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);
        doThrow(new ArticleExistsInFavoritesListException()).when(favouritesListValidation).exists(articleEntity1, userEntity);

        // Act and Assert
        assertThrows(ArticleExistsInFavoritesListException.class, () -> {
            favoritesListService.save(userId, articleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(articleId);
    }

    @Test
    void testSaveValidArticleToNonExistingUserFavouritesList_shouldThrowException() {
        // Arrange
        int invalidUserId = 999;
        int articleId = article1.getId();

        when(accountRepositoryMock.getAccountEntityById(invalidUserId)).thenReturn(invalidUser);
        doThrow(new AccountNotFoundException()).when(accountValidation).isUserValid(invalidUser);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            favoritesListService.save(invalidUserId, articleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidUserId);
    }

    @Test
    void testSaveValidArticleToInvalidAccountTypeFavouritesList_shouldThrowException() {
        // Arrange
        int invalidAccountId = 999;
        int articleId = article1.getId();

        when(accountRepositoryMock.getAccountEntityById(invalidAccountId)).thenReturn(journalistEntity);
        doThrow(new UserNotFoundException()).when(accountValidation).isUserValid(journalistEntity);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            favoritesListService.save(invalidAccountId, articleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidAccountId);
    }

    @Test
    void testDeleteValidArticleFromValidUserFavouritesList_shouldReturnNothing() {
        // Arrange
        int userId = userEntity.getId();
        int articleId = articleEntity1.getId();

        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);
        when(favouritesListRepositoryMock.findByUserAndArticle(userEntity, articleEntity1)).thenReturn(favouriteArticleEntity1);

        // Act
        favoritesListService.delete(userId, articleId);

        // Assert
        verify(accountRepositoryMock).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(articleId);
        verify(favouritesListRepositoryMock).findByUserAndArticle(userEntity, articleEntity1);
    }

    @Test
    void testDeleteNonExistingArticleFromFavouritesList_shouldThrowException() {
        // Arrange
        int userId = userEntity.getId();
        int invalidArticleId = 999;

        when(accountRepositoryMock.getAccountEntityById(userId)).thenReturn(userEntity);
        when(articleRepositoryMock.getArticleEntityById(invalidArticleId)).thenReturn(invalidArticle);
        doThrow(new ArticleNotFoundException()).when(articleValidation).isEmpty(invalidArticle);

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            favoritesListService.delete(userId, invalidArticleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(userId);
        verify(articleRepositoryMock).getArticleEntityById(invalidArticleId);
    }

    @Test
    void testDeleteValidArticleFromNonExistingUserFavouritesList_shouldThrowException() {
        // Arrange
        int invalidUserId = 999;
        int articleId = article1.getId();

        when(accountRepositoryMock.getAccountEntityById(invalidUserId)).thenReturn(invalidUser);
        doThrow(new AccountNotFoundException()).when(accountValidation).isUserValid(invalidUser);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            favoritesListService.delete(invalidUserId, articleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidUserId);
    }

    @Test
    void testDeleteValidArticleFromInvalidAccountTypeFavouritesList_shouldThrowException() {
        // Arrange
        int invalidAccountId = 999;
        int articleId = article1.getId();

        when(accountRepositoryMock.getAccountEntityById(invalidAccountId)).thenReturn(journalistEntity);
        doThrow(new UserNotFoundException()).when(accountValidation).isUserValid(journalistEntity);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            favoritesListService.delete(invalidAccountId, articleId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidAccountId);
    }
}
