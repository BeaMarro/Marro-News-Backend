package nl.fontys.newswebapplication.service.converter;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TestArticleConverter {
    @Mock
    private ArticleConverter articleConverter;

    private ArticleEntity articleEntity;
    private ArticleRequest articleRequest;
    private Article article;

    private JournalistEntity journalistEntity;

    @BeforeEach
    void setup() {
        int approvalStatusId = ApprovalStatus.APPROVED.ordinal();
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
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

        // Setup Article
        JournalistResponse journalist = JournalistResponse.builder()
                .id(2)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture("john_doe.jpg")
                .department(Department.COPYRIGHTING)
                .build();

        // Setup Article objects

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

        // Setup Article Request
        articleRequest = ArticleRequest.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .authorId(journalist.getId())
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.of("www.video.nl"))
                .coverImage("image.jpg")
                .build();

        // Setup Article Entity
        articleEntity = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();
    }

    @ParameterizedTest
    @MethodSource("provideForConvertToArticleEntity_shouldReturnArticleEntity_whenEmptyOrValidOptionalFieldsProvided")
    void testConvertArticleEntityToArticle_shouldReturnArticle(Optional<String> video, Optional<byte[]> coverImage) {
        // Arrange
        Article expected = article;

        if(coverImage.isPresent()) {
            articleEntity.setCoverImage(coverImage.get());
        } else {
            articleEntity.setCoverImage(null);
        }

        if(video.isPresent()) {
            articleEntity.setVideoLink(video.get());
        } else {
            articleEntity.setVideoLink(null);
        }

        // Act
        Article actual = ArticleConverter.convertEntityToArticle(articleEntity);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getHeading(), actual.getHeading());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        assertEquals(expected.getPublishDate(), actual.getPublishDate());
        assertEquals(expected.getGenre(), actual.getGenre());
        assertEquals(video, actual.getVideo());
    }

    private static Stream<Arguments> provideForConvertToArticleEntity_shouldReturnArticleEntity_whenEmptyOrValidOptionalFieldsProvided() {
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        return Stream.of(
                Arguments.of(Optional.of("www.video.nl"), Optional.of(coverImage)), // Valid video and cover
                Arguments.of(Optional.empty(), Optional.empty()) // Empty video and cover
        );
    }
}
