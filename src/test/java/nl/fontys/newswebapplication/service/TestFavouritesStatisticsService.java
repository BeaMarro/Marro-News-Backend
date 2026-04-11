package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.FavouritesListRepository;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.FavouritesStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestFavouritesStatisticsService {
    @Mock
    private FavouritesListRepository favouritesListRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private FavouritesStatisticsService favouritesStatisticsService;

    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        // Setup
        String encodedPassword = "Encoded Password";
        String profilePicture = "profile_picture.jpg";
        byte[] profilePictureBlob = profilePicture.getBytes();

        // User Entity
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 8, 9));
        userEntity.setProfilePicture(profilePictureBlob);
        userEntity.setPassword(encodedPassword);
        userEntity.setBio("Hello Marro News!");
    }

    @Test
    void testGetTotalArticlesPerGenre_shouldReturnStatistics() {
        // Arrange
        int userId = 1;
        when(accountRepository.findById(userId)).thenReturn(Optional.ofNullable(userEntity));

        for(Genre genre : Genre.values()) {
            when(favouritesListRepository.countTotalArticlesByGenreAndUser(userEntity, genre.ordinal())).thenReturn(genre.ordinal());
        }

        // Act
        Map<String, Integer> result = favouritesStatisticsService.getTotalArticlesPerGenre(userId);

        // Assert
        assertEquals(Genre.values().length, result.size()); // Ensure all genres are present in the result

        for (Genre genre : Genre.values()) {
            assertEquals(genre.ordinal(), result.get(genre.name()));
        }
    }
}
