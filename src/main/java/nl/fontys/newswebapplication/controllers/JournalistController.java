package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.services.AccountService;
import nl.fontys.newswebapplication.services.exceptions.account.*;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/journalists")
public class JournalistController {
    private AccountService service;
    private AccessToken requestAccessToken;

    @RolesAllowed({"ADMIN"})
    @GetMapping()
    public ResponseEntity<ArrayList<JournalistResponse>> getAll() {
        try {
            ArrayList<JournalistResponse> journalists = new ArrayList<>();

            for (AccountResponse account : this.service.getAll()) {
                if (account instanceof JournalistResponse journalist) { // Type checking and casting
                    journalists.add(journalist);
                }
            }

            return ResponseEntity.ok(journalists);
        } catch(AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<JournalistResponse> getById(@PathVariable("id") int id) {
        try {
            AccountResponse account = this.service.getById(id);

            if(account instanceof JournalistResponse) {
                JournalistResponse journalist = (JournalistResponse) this.service.getById(id);
                return ResponseEntity.ok(journalist);
            }
            else {
                return ResponseEntity.notFound().build(); // If retrieved account != Journalist -> Http Status 404
            }
        }
        // Checks Exception type and returns corresponding status
        catch (AccountNotFoundException exception) { // 404 Not Found
            return ResponseEntity.notFound().build();
        } catch (Exception exception) { // Generic other exception
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping()
    public ResponseEntity<Journalist> create(@RequestBody Journalist newJournalist) {
        Journalist journalist = (Journalist) service.save(newJournalist);

        try {
            return new ResponseEntity<>(journalist, HttpStatus.CREATED);
        } catch (AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (AccountExistsException exception) {
            return ResponseEntity.status(409).build();
        }catch (InvalidFieldFormatException | MissingFieldException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch(AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"JOURNALIST"})
    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody Journalist journalist) {
        try {
            if (requestAccessToken.getAccountId() != journalist.getId()) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            service.update(journalist);
            return ResponseEntity.noContent().build();
        }
        catch (AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(UnsupportedAccountTypeException | MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
