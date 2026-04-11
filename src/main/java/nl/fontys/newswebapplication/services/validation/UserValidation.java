package nl.fontys.newswebapplication.services.validation;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.stereotype.Component;

@Component
public class UserValidation extends AccountValidation {
    public UserValidation(AccountRepository repo) {
        super(repo);
    }

    public void checkUser(Account account) {
        User user = (User) account;
        isBioValid(user.getBio());
    }

    private void isBioValid(String bio) {
        if (bio == null) {
            throw new MissingFieldException("Bio not found");
        }
    }
}
