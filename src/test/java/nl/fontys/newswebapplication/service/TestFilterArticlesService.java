package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.Filter;
import nl.fontys.newswebapplication.services.FilterArticlesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TestFilterArticlesService {
    @Mock
    private Filter filter;
    @InjectMocks
    private FilterArticlesService filterArticlesService;

    private Article article1;
    private Article article2;
    private List<Article> articles = new ArrayList<>();

    @BeforeEach
    void setup() {
        // Setup Journalist object
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
        article1 = Article.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .author(journalist)
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.empty())
                .coverImage("hello.jpg")
                .status(ApprovalStatus.APPROVED)
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

        articles.add(article1);
        articles.add(article2);
    }

    @Test
    void getFilteredArticlesByValidHeading_shouldReturnAllFilteredApprovedArticlesConverted() {
        // Arrange
        String heading = article1.getHeading();
        List<Article> filteredArticles = List.of(article1);
        Article[] expected = filteredArticles.toArray(new Article[0]);

        // Act
        List<Article> actual = Arrays.stream(filterArticlesService.useFilter(articles.toArray(new Article[0]), heading)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredArticlesByValidHeadingSubstring_shouldReturnAllFilteredApprovedArticlesConverted() {
        // Arrange
        String heading = "2";
        List<Article> filteredArticles = List.of(article2);
        Article[] expected = filteredArticles.toArray(new Article[0]);

        // Act
        List<Article> actual = Arrays.stream(filterArticlesService.useFilter(articles.toArray(new Article[0]), heading)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredArticlesByInvalidHeadingSubstring_shouldReturnEmptyList() {
        // Arrange
        String heading = "Fontys";
        List<Article> filteredArticles = List.of();
        Article[] expected = filteredArticles.toArray(new Article[0]);

        // Act
        List<Article> actual = Arrays.stream(filterArticlesService.useFilter(articles.toArray(new Article[0]), heading)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredArticlesByValidTextSubstring_shouldReturnAllFilteredApprovedArticlesConverted() {
        // Arrange
        String text = "Text";
        List<Article> filteredArticles = List.of(article1, article2);
        Article[] expected = filteredArticles.toArray(new Article[0]);

        // Act
        List<Article> actual = Arrays.stream(filterArticlesService.useFilter(articles.toArray(new Article[0]), text)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredArticlesByInvalidTextSubstring_shouldReturnEmptyList() {
        // Arrange
        String text = "The Netherlands";
        List<Article> filteredArticles = List.of();
        Article[] expected = filteredArticles.toArray(new Article[0]);

        // Act
        List<Article> actual = Arrays.stream(filterArticlesService.useFilter(articles.toArray(new Article[0]), text)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }
}
