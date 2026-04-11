package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.services.ApprovalService;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestApprovalService {
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
    private SimpMessagingTemplate template;
    @InjectMocks
    private ArticleService articleService;
    @InjectMocks
    private ApprovalService approvalService;

    private ArticleEntity articleEntity1;
    private ArticleEntity articleEntity2;
    private JournalistEntity journalistEntity;

    private Article pendingArticle1;
    private Article pendingArticle2;
    private Article approvedArticle;
    private Article disapprovedArticle;
    private ArticleEntity invalidArticle;

    private JournalistResponse journalist;
    private int pendingStatus = 0;

    @BeforeEach
    void setUp() {
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

        // Setup Article Entities

        articleEntity1 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalistEntity)
                .genreId(2)
                .videoLink(null)
                .coverImage(coverImage)
                .approvalStatusId(pendingStatus)
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
                .approvalStatusId(pendingStatus)
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

        pendingArticle1 = Article.builder()
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

        pendingArticle2 = Article.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(LocalDate.now())
                .author(journalist)
                .genre(Genre.POLITICS)
                .video(Optional.of("www.video.nl"))
                .coverImage("hello.jpg")
                .status(ApprovalStatus.PENDING)
                .build();

        approvedArticle = Article.builder()
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

        disapprovedArticle = Article.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .author(journalist)
                .genre(Genre.BUSINESS_ECONOMICS)
                .video(Optional.empty())
                .coverImage("image.jpg")
                .status(ApprovalStatus.DISAPPROVED)
                .build();
    }

    @Test
    void testGetAllPendingArticles_shouldReturnAllPendingArticlesConverted() {
        when(articleRepositoryMock.getArticleEntitiesByApprovalStatusId(pendingStatus)).thenReturn(List.of(articleEntity1, articleEntity2));

        List<Article> actualResult = approvalService.getAllPendingArticles();
        List<Article> expectedResult = List.of(pendingArticle1, pendingArticle2);

        assertEquals(expectedResult.size(), actualResult.size());

        verify(articleRepositoryMock).getArticleEntitiesByApprovalStatusId(pendingStatus);
    }

    @Test
    void testSetValidArticleStatusToApproved_shouldReturnNothing() {
        // Arrange
        int articleId = pendingArticle1.getId();
        ApprovalStatus status = ApprovalStatus.APPROVED;
        Article expected = approvedArticle;

        // Act
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);

        when(articleRepositoryMock.save(articleEntity1)).thenReturn(articleEntity1); // Update

        approvalService.changeApprovalStatus(articleId, status); // Update
        Article actual = articleService.getById(articleId);

        // Assert
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());

        verify(articleRepositoryMock, times(2)).getArticleEntityById(articleId);
        verify(articleRepositoryMock).save(articleEntity1);
    }

    @Test
    void testSetValidArticleStatusToDisapproved_shouldReturnNothing() {
        // Arrange
        int articleId = pendingArticle1.getId();
        ApprovalStatus status = ApprovalStatus.DISAPPROVED;
        Article expected = disapprovedArticle;

        // Act
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);

        when(articleRepositoryMock.save(articleEntity1)).thenReturn(articleEntity1); // Update

        approvalService.changeApprovalStatus(articleId, status); // Update
        Article actual = articleService.getById(articleId);

        // Assert
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());

        verify(articleRepositoryMock, times(2)).getArticleEntityById(articleId);
        verify(articleRepositoryMock).save(articleEntity1);
    }

    @Test
    void testSetValidArticleStatusToPending_shouldReturnNothing() {
        // Arrange
        int articleId = pendingArticle1.getId();
        ApprovalStatus status = ApprovalStatus.PENDING;
        Article expected = pendingArticle1;

        // Act
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(articleEntity1);

        when(articleRepositoryMock.save(articleEntity1)).thenReturn(articleEntity1); // Update

        approvalService.changeApprovalStatus(articleId, status); // Update
        Article actual = articleService.getById(articleId);

        // Assert
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());

        verify(articleRepositoryMock, times(2)).getArticleEntityById(articleId);
        verify(articleRepositoryMock).save(articleEntity1);
    }

    @Test
    void testSetStatusToInvalidArticle_shouldThrowException() {
        // Arrange
        int articleId = pendingArticle1.getId();
        ApprovalStatus status = ApprovalStatus.PENDING;

        // Act and Assert
        when(articleRepositoryMock.getArticleEntityById(articleId)).thenReturn(invalidArticle);
        doThrow(new ArticleNotFoundException()).when(articleValidation).isEmpty(invalidArticle);
        
        assertThrows(ArticleNotFoundException.class, () -> {
            approvalService.changeApprovalStatus(articleId, status);
        });

        verify(articleRepositoryMock, times(1)).getArticleEntityById(articleId);
    }
}
