package nl.fontys.newswebapplication.controllers;

import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.services.ReadingTimeService;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("articles")
public class ReadingTimeController {
    private ReadingTimeService service;

    @GetMapping("{id}/reading-time")
    public ResponseEntity<Integer> getById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.service.getReadingTimeByArticleId(id));
        }
        catch (ArticleNotFoundException exception) { // 404 Not Found
            return ResponseEntity.notFound().build();
        } catch (Exception exception) { // Generic other exception
            return ResponseEntity.internalServerError().build();
        }
    }
}
