package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.services.converter.ArticleConverter;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ApprovalService {
    private final ArticleRepository articleRepository;
    private final ArticleValidation articleValidation;

    private final SimpMessagingTemplate template;

    public List<Article> getAllPendingArticles() {
        List<Article> articles = new ArrayList<>();

        for(ArticleEntity articleEntity : articleRepository.getArticleEntitiesByApprovalStatusId(0)) { // Checks if the article status is pending - Only pending articles should be returned (0 - PENDING)
            Article article = ArticleConverter.convertEntityToArticle(articleEntity);
            articles.add(article);
        }

        return articles;
    }

    public void changeApprovalStatus(int articleId, ApprovalStatus status) {
        ArticleEntity article = articleRepository.getArticleEntityById(articleId);

        // Validate article
        articleValidation.isEmpty(article);

        //  Edit the approval status property
        article.setApprovalStatusId(status.ordinal());
        // Send notification via WebSockets
        template.convertAndSend("/topic/user",
                "The article: '" + article.getHeading() + "' status has been changed by your administrator to " + status.toString().toLowerCase());
        // Save updated version
        articleRepository.save(article);
    }
}
