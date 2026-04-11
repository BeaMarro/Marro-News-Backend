package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.services.FavoritesListService;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.InvalidAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.account.user.UserNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.favourites.ArticleExistsInFavoritesListException;
import nl.fontys.newswebapplication.services.exceptions.favourites.FavouriteArticleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/favourites")
public class FavoritesListController {
    private FavoritesListService service;
    private AccessToken requestAccessToken;

    @RolesAllowed({"USER"})
    @GetMapping()
    public ResponseEntity<List<Article>> getAll() {
        try {
            int userId = requestAccessToken.getAccountId();
            if (requestAccessToken.getAccountId() != userId) { // Only logged-in user can update their data
                return (ResponseEntity<List<Article>>) ResponseEntity.status(403);
            }
            return ResponseEntity.ok(this.service.getById(userId));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch(UnsupportedAccountTypeException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"USER"})
    @PostMapping("{articleId}")
    public ResponseEntity<List<Article>> create(@PathVariable("articleId") int articleId) {
        try {
            int userId = requestAccessToken.getAccountId();
            if (requestAccessToken.getAccountId() != userId) { // Only logged-in user can update their data
                return (ResponseEntity<List<Article>>) ResponseEntity.status(403);
            }
            return ResponseEntity.ok(this.service.save(userId, articleId));
        } catch (ArticleExistsInFavoritesListException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch(InvalidAccountTypeException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"USER"})
    @DeleteMapping("{articleId}")
    public ResponseEntity<Void> delete(@PathVariable("articleId") int articleId) {
        try {
            int userId = requestAccessToken.getAccountId();
            if (requestAccessToken.getAccountId() != userId) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            this.service.delete(userId, articleId);
            return ResponseEntity.noContent().build();
        } catch (FavouriteArticleNotFoundException | ArticleNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch(InvalidAccountTypeException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
