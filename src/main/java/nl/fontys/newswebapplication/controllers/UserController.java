package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.config.security.token.exception.UnauthorizedDataAccessException;
import nl.fontys.newswebapplication.domain.*;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.UserResponse;
import nl.fontys.newswebapplication.services.AccountService;
import nl.fontys.newswebapplication.services.exceptions.account.AccountExistsException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private AccountService service;
    private AccessToken requestAccessToken;

    @RolesAllowed({"ADMIN"})
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAll() {
        try {
            ArrayList<UserResponse> users = new ArrayList<>();

            for (AccountResponse account : this.service.getAll()) {
                if (account instanceof UserResponse user) {
                    users.add(user);
                }
            }

            return ResponseEntity.ok(users);
        }
        catch(AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"USER"})
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("id") int id) {
        try {
            if (requestAccessToken.getAccountId() != id) { // Only logged-in user can update their data
                return (ResponseEntity<UserResponse>) ResponseEntity.status(403);
            }
            AccountResponse account = this.service.getById(id);

            if (account instanceof UserResponse user) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(UnauthorizedDataAccessException exception) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User newUser) {
        User user = (User) service.save(newUser);

        try {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (AccountExistsException exception) {
            return ResponseEntity.status(409).build();
        } catch(MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"USER"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        if (requestAccessToken.getAccountId() != id) { // Only logged-in user can update their data
            return (ResponseEntity<Void>) ResponseEntity.status(403);
        }
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"USER"})
    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody User user) {
        try {
            if (requestAccessToken.getAccountId() != user.getId()) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            service.update(user);
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException exception){
            return ResponseEntity.notFound().build();
        } catch(MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception){
            return ResponseEntity.internalServerError().build();
        }
    }
}
