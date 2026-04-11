package nl.fontys.newswebapplication.services.validation;

import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.services.exceptions.article.ArticlesNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticlesListValidation {
    public void isEmpty(List<ArticleEntity> articles) {
        if(articles == null) {
            throw new ArticlesNotFoundException("Articles not found");
        }
    }
}
