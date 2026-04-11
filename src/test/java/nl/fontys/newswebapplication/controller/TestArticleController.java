package nl.fontys.newswebapplication.controller;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestArticleController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private JournalistResponse journalist;
    private Article article1;
    private Article article2;
    private Article article3;

    @BeforeEach
    void setup() {
        journalist = JournalistResponse.builder()
                .id(2)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture("john_doe.jpg")
                .department(Department.COPYRIGHTING)
                .build();

        LocalDate date = LocalDate.of(2023, 11, 27);

        article1 = Article.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(date)
                .author(journalist)
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.empty())
                .coverImage("image.jpg")
                .status(ApprovalStatus.APPROVED)
                .build();

        article2 = Article.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(date)
                .author(journalist)
                .genre(Genre.POLITICS)
                .video(Optional.of("www.video.nl"))
                .coverImage("hello.jpg")
                .status(ApprovalStatus.APPROVED)
                .build();

        article3 = Article.builder()
                .id(3)
                .heading("Heading 3")
                .text("Text 3")
                .publishDate(date)
                .author(journalist)
                .genre(Genre.POLITICS)
                .video(Optional.of("www.video.nl"))
                .coverImage("greetings.jpg")
                .status(ApprovalStatus.APPROVED)
                .build();
    }

    @Test
    void testGetAllArticles_shouldReturn200WithAllApprovedArticles_whenFound() throws Exception {
        // Arrange
        List<Article> articles = List.of(article1, article2);

        when(articleService.getAll()).thenReturn(articles);

        // Act and Assert
        mockMvc.perform(get("/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                [
                    {
                        "id": 1,
                        "heading": "Heading 1",
                        "text": "Text 1",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "BUSINESS_ECONOMICS",
                        "video": null,
                        "coverImage": "image.jpg",
                        "status": "APPROVED"
                    },
                    {
                        "id": 2,
                        "heading": "Heading 2",
                        "text": "Text 2",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "POLITICS",
                        "video": "www.video.nl",
                        "coverImage": "hello.jpg",
                        "status": "APPROVED"
                    }
                ]
            """));

        verify(articleService).getAll();
    }

    @Test
    void testGetArticleById_shouldReturn200WithApprovedArticle_whenFound() throws Exception {
        // Arrange
        int id = 1;

        when(articleService.getById(id)).thenReturn(article1);

        // Act and Assert
        mockMvc.perform(get("/articles/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {
                    "id": 1,
                    "heading": "Heading 1",
                    "text": "Text 1",
                    "publishDate": "2023-11-27",
                    "author": {
                        "id": 2,
                        "fullName": "John Doe",
                        "username": "J.Doe",
                        "email": "j.doe@news.com",
                        "dateOfBirth": "1985-06-09",
                        "profilePicture": "john_doe.jpg",
                        "department": "COPYRIGHTING"
                    },
                    "genre": "BUSINESS_ECONOMICS",
                    "video": null,
                    "coverImage": "image.jpg",
                    "status": "APPROVED"
                }
            """));

        verify(articleService).getById(id);
    }

    @Test
    void testGetArticleByInvalidId_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        int invalidId = 999;

        when(articleService.getById(invalidId)).thenThrow(new ArticleNotFoundException("Article not found"));

        // Act and Assert
        mockMvc.perform(get("/articles/" + invalidId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(articleService).getById(invalidId);
    }

    @Test
    void testGetAllArticlesByGenre_shouldReturn200WithAllApprovedArticles_whenFound() throws Exception {
        // Arrange
        Genre genre = Genre.POLITICS;
        List<Article> articles = List.of(article1, article3);

        when(articleService.getByGenre(genre)).thenReturn(articles);

        // Act and Assert
        mockMvc.perform(get("/articles?genre=" + genre))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                [
                    {
                        "id": 1,
                        "heading": "Heading 1",
                        "text": "Text 1",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "BUSINESS_ECONOMICS",
                        "video": null,
                        "coverImage": "image.jpg",
                        "status": "APPROVED"
                    },
                    {
                        "id": 3,
                        "heading": "Heading 3",
                        "text": "Text 3",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "POLITICS",
                        "video": "www.video.nl",
                        "coverImage": "greetings.jpg",
                        "status": "APPROVED"
                    }
                ]
            """));

        verify(articleService).getByGenre(genre);
    }

    @Test
    void testGetAllArticlesByValidJournalist_shouldReturn200WithAllApprovedArticles_whenFound() throws Exception {
        // Arrange
        int journalistId = journalist.getId();
        List<Article> articles = List.of(article1, article2, article3);

        when(articleService.getByJournalist(journalistId)).thenReturn(articles);

        // Act and Assert
        mockMvc.perform(get("/articles/all?journalistId=" + journalistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                [
                    {
                        "id": 1,
                        "heading": "Heading 1",
                        "text": "Text 1",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "BUSINESS_ECONOMICS",
                        "video": null,
                        "coverImage": "image.jpg",
                        "status": "APPROVED"
                    },
                    {
                        "id": 2,
                        "heading": "Heading 2",
                        "text": "Text 2",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "POLITICS",
                        "video": "www.video.nl",
                        "coverImage": "hello.jpg",
                        "status": "APPROVED"
                    },
                    {
                        "id": 3,
                        "heading": "Heading 3",
                        "text": "Text 3",
                        "publishDate": "2023-11-27",
                        "author": {
                            "id": 2,
                            "fullName": "John Doe",
                            "username": "J.Doe",
                            "email": "j.doe@news.com",
                            "dateOfBirth": "1985-06-09",
                            "profilePicture": "john_doe.jpg",
                            "department": "COPYRIGHTING"
                        },
                        "genre": "POLITICS",
                        "video": "www.video.nl",
                        "coverImage": "greetings.jpg",
                        "status": "APPROVED"
                    }
                ]
            """));

        verify(articleService).getByJournalist(journalistId);
    }
}