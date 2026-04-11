
package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.favourites.FavouriteArticleNotFoundException;
import nl.fontys.newswebapplication.services.validation.FavouritesListValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestFavouritesListValidation {
    @Mock
    private FavouritesListRepository favouritesListRepositoryMock;
    @InjectMocks
    private FavouritesListValidation favouritesListValidation;

    private ArticleEntity articleEntity;
    private UserEntity userEntity;

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

        // User
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 8, 9));
        userEntity.setProfilePicture(profilePictureBlob);
        userEntity.setPassword("BMarro1234");
        userEntity.setBio("test");

        // Article
        articleEntity = ArticleEntity.builder()
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
    }

    @Test
    void testNonExistingArticleExistsInFavouritesList_shouldReturnNothing() {
        // Arrange
        when(favouritesListRepositoryMock.existsByUserAndArticle(userEntity, articleEntity)).thenReturn(false);

        // Act and Assert
        assertDoesNotThrow(() -> favouritesListValidation.exists(articleEntity, userEntity));
    }

    @Test
    void testExistingArticleExistsInFavouritesList_shouldThrowException() {
        // Arrange
        when(favouritesListRepositoryMock.existsByUserAndArticle(userEntity, articleEntity)).thenReturn(true);

        // Act and Assert
        assertThrows(FavouriteArticleNotFoundException.class, () -> {
            favouritesListValidation.exists(articleEntity, userEntity);
        });
    }
}
