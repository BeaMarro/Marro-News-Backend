package nl.fontys.newswebapplication.controllers;

import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.services.AccountService;
import nl.fontys.newswebapplication.services.FilterAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/journalists")
public class SearchJournalistController {
    private AccountService accountService;
    private FilterAccountService filterAccountService;

    @GetMapping("/search")
    public ResponseEntity<List<JournalistResponse>> searchJournalist(@RequestParam("query") String query) {
        try {
            AccountResponse[] accounts = filterAccountService.useFilter(accountService.getAll().toArray(new AccountResponse[0]), query);

            List<JournalistResponse> journalists = new ArrayList<>();

            for(AccountResponse account : accounts) {
                if(account instanceof JournalistResponse journalistResponse) {
                    journalists.add(journalistResponse);
                }
            }

            return ResponseEntity.ok().body(journalists);
        } catch (Exception exception) { // Generic other exception
            return ResponseEntity.internalServerError().build();
        }
    }
}
