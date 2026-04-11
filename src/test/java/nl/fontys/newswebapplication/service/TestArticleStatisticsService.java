package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.ArticleStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestArticleStatisticsService {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleStatisticsService articleStatisticsService;

    private final List<AccountEntity> accounts = new ArrayList<>();
    private JournalistEntity journalist1;
    private JournalistEntity journalist2;
    private JournalistEntity journalist3;

    @BeforeEach
    void setup() {
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Setup Journalist Entities
        journalist1 = new JournalistEntity();
        journalist1.setId(1);
        journalist1.setFullName("John Doe");
        journalist1.setUsername("J.Doe");
        journalist1.setEmail("j.doe@news.com");
        journalist1.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalist1.setProfilePicture(profilePicture);
        journalist1.setPassword("12345678910");
        journalist1.setDepartmentId(2);

        journalist2 = new JournalistEntity();
        journalist2.setId(2);
        journalist2.setFullName("Jane Doe");
        journalist2.setUsername("Jane.Doe");
        journalist2.setEmail("jane.doe@news.com");
        journalist2.setDateOfBirth(LocalDate.of(2001, 6, 9));
        journalist2.setProfilePicture(profilePicture);
        journalist2.setPassword("12345678");
        journalist2.setDepartmentId(1);

        journalist3 = new JournalistEntity();
        journalist2.setId(3);
        journalist2.setFullName("Jake Doe");
        journalist2.setUsername("Jake.Doe");
        journalist2.setEmail("jake.doe@news.com");
        journalist2.setDateOfBirth(LocalDate.of(1994, 6, 9));
        journalist2.setProfilePicture(profilePicture);
        journalist2.setPassword("123456789");
        journalist2.setDepartmentId(3);

        accounts.add(journalist1);
        accounts.add(journalist2);
        accounts.add(journalist3);
    }

    @Test
    void testGetTotalArticlesByJournalist_shouldReturnTotalArticles() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(accounts);
        when(articleRepository.countTotalArticlesByJournalist(journalist1.getId())).thenReturn(1);
        when(articleRepository.countTotalArticlesByJournalist(journalist2.getId())).thenReturn(2);
        when(articleRepository.countTotalArticlesByJournalist(journalist3.getId())).thenReturn(3);
        // Act
        Map<String, Integer> statistics = articleStatisticsService.getTotalArticlesByJournalists();
        // Assert
        assertEquals(3, statistics.size());
        assertEquals(1, statistics.get(journalist1.getFullName()));
        assertEquals(2, statistics.get(journalist2.getFullName()));
        assertEquals(3, statistics.get(journalist3.getFullName()));
    }

    @Test
    void testGetArticleShareByJournalist_shouldReturnTotalShare() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(accounts);
        when(articleRepository.findArticleShareByJournalist(journalist1.getId())).thenReturn(25.0);
        when(articleRepository.findArticleShareByJournalist(journalist2.getId())).thenReturn(45.0);
        when(articleRepository.findArticleShareByJournalist(journalist3.getId())).thenReturn(30.0);
        // Act
        Map<String, Double> statistics = articleStatisticsService.getArticleShareByJournalists();
        // Assert
        assertEquals(3, statistics.size());
        assertEquals(25, statistics.get(journalist1.getFullName()));
        assertEquals(45, statistics.get(journalist2.getFullName()));
        assertEquals(30, statistics.get(journalist3.getFullName()));
    }
}
