package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.services.ArticleStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/articles/statistics")
public class ArticleStatisticsController {
    private ArticleStatisticsService service;

    @RolesAllowed({"ADMIN"})
    @GetMapping("/total")
    public ResponseEntity<Map<String, Integer>> getTotalArticlesByAllJournalists() {
        try {
            return ResponseEntity.ok(this.service.getTotalArticlesByJournalists());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/share")
    public ResponseEntity<Map<String, Double>> getArticleShareByAllJournalists() {
        try {
            return ResponseEntity.ok(this.service.getArticleShareByJournalists());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
