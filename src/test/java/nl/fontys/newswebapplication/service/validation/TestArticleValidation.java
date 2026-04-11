package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleExistsException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestArticleValidation {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private ArticleValidation articleValidation;

    private Article article;
    private ArticleRequest articleRequest;
    private ArticleEntity articleEntity;

    private Article emptyArticle;
    private ArticleRequest emptyArticleRequest;
    private ArticleEntity emptyArticleEntity;

    private JournalistResponse journalist;
    private JournalistEntity journalistEntity;

    @BeforeEach
    void setup() {
        int approvalStatusId = ApprovalStatus.APPROVED.ordinal();
        byte[] coverImage = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String profilePicture = "profile_picture.jpg";
        byte[] profilePictureBlob = profilePicture.getBytes();

        // Journalist
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(2);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePictureBlob);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        journalist = JournalistResponse.builder()
                .id(1)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture("john_doe.jpg")
                .department(Department.COPYRIGHTING)
                .build();

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

        articleRequest = ArticleRequest.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .authorId(journalist.getId())
                .genre(Genre.TRAVEL)
                .video(Optional.empty())
                .coverImage(Arrays.toString(coverImage))
                .build();

        article = Article.builder()
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

        emptyArticleEntity = null;
        emptyArticleRequest = null;
        emptyArticle = null;
    }

    @Test
    void testValidArticleIsEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isEmpty(article));
    }

    @Test
    void testValidArticleEntityIsEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isEmpty(articleEntity));
    }

    @Test
    void testValidArticleRequestIsEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isEmpty(articleRequest));
    }

    @Test
    void testEmptyArticleIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleValidation.isEmpty(emptyArticle);
        });
    }

    @Test
    void testEmptyArticleEntityIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleValidation.isEmpty(emptyArticleEntity);
        });
    }

    @Test
    void testEmptyArticleRequestIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleValidation.isEmpty(emptyArticleRequest);
        });
    }

    @Test
    void testExistsByNonExistingHeading_shouldReturnNothing() {
        // Assert
        String invalidHeading = "Heading";
        when(articleRepository.existsByHeading(invalidHeading)).thenReturn(false);

        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.existsByHeading(invalidHeading));
    }

    @Test
    void testExistsByExistingHeading_shouldThrowException() {
        // Assert
        String validHeading = "Giorgia Meloni visits the Job&Orienta Fair";
        when(articleRepository.existsByHeading(validHeading)).thenReturn(true);

        // Act and Assert
        assertThrows(ArticleExistsException.class, () -> {
            articleValidation.existsByHeading(validHeading);
        });
    }

    @Test
    void testValidHeadingIsEmpty_shouldReturnNothing() {
        // Arrange
        String validHeading = "Giorgia Meloni visits the Job&Orienta Fair";
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isHeadingLengthValid(validHeading));
    }

    @Test
    void testEmptyHeadingIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            articleValidation.isHeadingLengthValid(null);
        });
    }

    @Test
    void testInvalidHeadingLengthIsValid_shouldThrowException() {
        String heading = "m";

        assertThrows(InvalidFieldFormatException.class, () ->
                articleValidation.isHeadingLengthValid(heading)
        );
    }

    @Test
    void testEmptyArticleTextIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            articleValidation.isTextLengthValid(null);
        });
    }

    @Test
    void testInvalidArticleTextLengthIsValid_shouldThrowException() {
        // Arrange
        String text = "Beatrice Marro";

        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () ->
                articleValidation.isTextLengthValid(text)
        );
    }

    @Test
    void testIsValidGenreIdValid_shouldReturnNothing() {
        // Arrange
        int validGenreId = 1;
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isGenreValid(validGenreId));
    }

    @Test
    void testIsInvalidGenreIdValid_shouldThrowException() {
        // Arrange
        int invalidGenreId = 999;
        // Act and Assert
        assertThrows(MissingFieldException.class, () ->
                articleValidation.isGenreValid(invalidGenreId)
        );
    }

    @Test
    void testIsValidVideoLinkValid_shouldReturnNothing() {
        // Arrange
        String validVideoLink = "https://www.youtube.com/embed/gA8RMj1l0yg?si=LldcUsheiYxRyS8a";
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isVideoLinkValid(Optional.of(validVideoLink)));
    }

    @Test
    void testIsVideoLinkValidWhenNotSupplied_shouldReturnNothing() {
        // Arrange
        Optional<String> emptyVideo = Optional.empty();
        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isVideoLinkValid(emptyVideo));
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidVideoLinksAreSupplied")
    void testInvalidVideoLinkIsValid_shouldThrowException(Optional<String> video) {
        assertThrows(InvalidFieldFormatException.class, () ->
                articleValidation.isVideoLinkValid(video)
        );
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidVideoLinksAreSupplied() {
        return Stream.of(
                Arguments.of(Optional.of("https://www.youtube.com/embed/")),
                Arguments.of(Optional.of("https://www.youtube.com/abcd/efgh"))
        );
    }

    @Test
    void testIsValidCoverImageValid_shouldReturnNothing() {
        // Arrange
        byte[] coverImage = "cover_picture.jpg".getBytes();

        // Act and Assert
        assertDoesNotThrow(() -> articleValidation.isCoverImageValid(Arrays.toString(coverImage)));
    }

    @Test
    void testIsInvalidCoverImageValid_shouldReturnNothing() {
        // Arrange
        String coverImage = null;

        // Act and Assert
        assertThrows(MissingFieldException.class, () ->
                articleValidation.isCoverImageValid(coverImage)
        );
    }
}
