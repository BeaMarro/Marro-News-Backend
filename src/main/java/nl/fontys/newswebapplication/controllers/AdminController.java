package nl.fontys.newswebapplication.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.AdminResponse;
import nl.fontys.newswebapplication.services.AccountService;
import nl.fontys.newswebapplication.services.exceptions.account.AccountExistsException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Generated
@CrossOrigin("http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private AccountService service;
    private AccessToken requestAccessToken;

    @RolesAllowed({"ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<AdminResponse> getById(@PathVariable("id") int id) {
        try {
            if (requestAccessToken.getAccountId() != id) { // Only logged-in user can update their data
                return (ResponseEntity<AdminResponse>) ResponseEntity.status(403);
            }
            AccountResponse account = this.service.getById(id);

            if(account instanceof AdminResponse) {
                AdminResponse admin = (AdminResponse) this.service.getById(id);
                return ResponseEntity.ok(admin);
            }
            else {
                return ResponseEntity.notFound().build(); // If retrieved account != Admin -> Http Status 404
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
    public ResponseEntity<Admin> create(@RequestBody Admin newAdmin) {

        Admin admin = (Admin) service.save(newAdmin);

        try {
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        } catch (AccountExistsException exception) {
            return ResponseEntity.notFound().build();
        } catch(MissingFieldException | InvalidFieldFormatException exception) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {

        try {
            if (requestAccessToken.getAccountId() != id) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(AccountNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch(Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RolesAllowed({"ADMIN"})
    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody Admin admin) {
        try {
            if (requestAccessToken.getAccountId() != admin.getId()) { // Only logged-in user can update their data
                return (ResponseEntity<Void>) ResponseEntity.status(403);
            }
            service.update(admin);
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
