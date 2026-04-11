package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.services.ApprovalService;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ApprovalController {
    private ApprovalService service;

    @RolesAllowed({"ADMIN"})
    @GetMapping("/pending")
    public ResponseEntity<List<Article>> getAll() {
        return ResponseEntity.ok(this.service.getAllPendingArticles());
    }

    @RolesAllowed({"ADMIN"})
    @PatchMapping("/approve/{id}")
    public ResponseEntity<Void> approveStatus(@PathVariable("id") int articleId) {
        try {
            service.changeApprovalStatus(articleId, ApprovalStatus.APPROVED);
            return ResponseEntity.noContent().build();
        } catch (ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception){
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @PatchMapping("/disapprove/{id}")
    public ResponseEntity<Void> disapproveStatus(@PathVariable("id") int articleId) {
        try {
            service.changeApprovalStatus(articleId, ApprovalStatus.DISAPPROVED);
            return ResponseEntity.noContent().build();
        } catch (ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception){
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @PatchMapping("/pending/{id}")
    public ResponseEntity<Void> pendingStatus(@PathVariable("id") int articleId) {
        try {
            service.changeApprovalStatus(articleId, ApprovalStatus.PENDING);
            return ResponseEntity.noContent().build();
        } catch (ArticleNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception){
            return ResponseEntity.internalServerError().build();
        }
    }
}
