package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.services.FavouritesStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/favourites_statistics")
public class FavouritesStatisticsController {
    private FavouritesStatisticsService service;
    private AccessToken requestAccessToken;

    @RolesAllowed({"USER"})
    @GetMapping()
    public ResponseEntity<Map<String, Integer>> getTotalFavouriteArticlesByGenre() {
        try {
            int accountId = requestAccessToken.getAccountId();
            return ResponseEntity.ok(this.service.getTotalArticlesPerGenre(accountId));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
