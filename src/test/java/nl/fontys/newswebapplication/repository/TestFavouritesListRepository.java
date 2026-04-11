package nl.fontys.newswebapplication.repository;

import jakarta.transaction.Transactional;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestFavouritesListRepository {
    @Mock
    private FavouritesListRepository favouritesListRepository;

    private ArticleEntity article1;
    private ArticleEntity article2;
    private UserEntity user;

    @BeforeEach
    void setup() {
        int approvalStatusId = ApprovalStatus.APPROVED.ordinal();
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Setup Journalist Entity
        JournalistEntity journalist = new JournalistEntity();
        journalist.setId(2);
        journalist.setFullName("John Doe");
        journalist.setUsername("J.Doe");
        journalist.setEmail("j.doe@news.com");
        journalist.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalist.setProfilePicture(profilePicture);
        journalist.setPassword("12345678910");
        journalist.setDepartmentId(2);

        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

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

        article2 = ArticleEntity.builder()
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

        // User Entity
        user = new UserEntity();
        user.setId(2);
        user.setFullName("Beatrice Marro");
        user.setUsername("B.Marro");
        user.setEmail("b.marro@student.fontys.nl");
        user.setDateOfBirth(LocalDate.of(2005, 8, 9));
        user.setProfilePicture(profilePicture);
        user.setPassword("Beatrice12345");
        user.setBio("test");
    }

    @Test
    void testGetTotalArticlesByGenreAndUser_shouldReturnArticlesCount() {
        // Arrange
        Genre genre = Genre.ART;
        int expected = 12;
        when(favouritesListRepository.countTotalArticlesByGenreAndUser(user, genre.ordinal())).thenReturn(expected);
        // Act
        int actual = favouritesListRepository.countTotalArticlesByGenreAndUser(user, genre.ordinal());
        // Assert
        assertEquals(expected, actual);
    }
}
