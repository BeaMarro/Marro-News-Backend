package nl.fontys.newswebapplication.controllers;

import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.FilterArticlesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class SearchArticleController {
    private ArticleService articleService;
    private FilterArticlesService filterArticlesService;

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticle(@RequestParam("query") String query) {
        try {
            return ResponseEntity.ok().body(Arrays.stream(filterArticlesService.useFilter(articleService.getAll().toArray(new Article[0]), query)).toList());
        } catch (Exception exception) { // Generic other exception
            return ResponseEntity.internalServerError().build();
        }
    }
}
