package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.services.ArticleService;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleExistsException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.journalist.JournalistNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private ArticleService service;
    private AccessToken requestAccessToken;

    @GetMapping()
    public ResponseEntity<List<Article>> getAll() {
        return ResponseEntity.ok(this.service.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Article> getById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.service.getById(id));
        }
        // Checks Exception type and returns corresponding status
        catch (ArticleNotFoundException exception) { // 404 Not Found
            return ResponseEntity.notFound().build();
        } catch (Exception exception) { // Generic other exception
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(params = "genre")
    public ResponseEntity<List<Article>> getByGenre(@RequestParam("genre") Genre genre)
    {
        try {
            return ResponseEntity.ok(service.getByGenre(genre));
        } catch (IllegalArgumentException exception) { // Genre does not exist
            return ResponseEntity.badRequest().build();
        }
        catch(ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/all", params = "journalistId")
    public ResponseEntity<List<Article>> getAllArticlesByJournalist(@RequestParam("journalistId") int journalistId)
    {
        try {
            return ResponseEntity.ok(service.getByJournalist(journalistId));
        } catch(JournalistNotFoundException | ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(params = "journalistId")
    public ResponseEntity<List<Article>> getAcceptedArticlesByJournalist(@RequestParam("journalistId") int journalistId)
    {
        try {
            return ResponseEntity.ok(service.getApprovedArticlesByJournalist(journalistId));
        } catch(JournalistNotFoundException | ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"JOURNALIST"})
    @PostMapping()
    public ResponseEntity<Article> create(@RequestBody ArticleRequest newArticle) {
        Article article = null;

        try {
            int loginId = requestAccessToken.getAccountId();
            if (loginId != newArticle.getAuthorId()) { // Only logged-in user can update their data
                return (ResponseEntity<Article>) ResponseEntity.status(403);
            }
            article = service.save(newArticle);
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        } catch (JournalistNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(ArticleExistsException exception) {
            return new ResponseEntity<>(article, HttpStatus.CONFLICT);
        } catch(MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"JOURNALIST"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {

        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"JOURNALIST"})
    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody ArticleRequest updatedArticle) {
        try {
            int loginId = requestAccessToken.getAccountId();
            if (loginId != updatedArticle.getAuthorId()) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            service.update(updatedArticle);
            return ResponseEntity.noContent().build();
        } catch (ArticleNotFoundException exception){
            return ResponseEntity.notFound().build();
        } catch(MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception){
            return ResponseEntity.internalServerError().build();
        }
    }
}
