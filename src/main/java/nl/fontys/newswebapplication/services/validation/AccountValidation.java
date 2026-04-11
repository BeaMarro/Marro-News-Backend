package nl.fontys.newswebapplication.services.validation;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountExistsException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.journalist.JournalistNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountValidation {
    private final AccountRepository repo;
    private final AdminValidation adminValidation;
    private final JournalistValidation journalistValidation;
    private final UserValidation userValidation;

    public AccountValidation(AccountRepository repo) {
        this.repo = repo;
        this.adminValidation = null;
        this.journalistValidation = null;
        this.userValidation = null;
    }

    @Autowired
    public AccountValidation(AccountRepository repo, AdminValidation adminValidation, JournalistValidation journalistValidation, UserValidation userValidation) {
        this.repo = repo;
        this.adminValidation = adminValidation;
        this.journalistValidation = journalistValidation;
        this.userValidation = userValidation;
    }

    public void validateAccount(Account account) {
        isEmpty(account);
        isAccountTypeValid(account);
        validateAccountFields(account);

        if (account instanceof Admin) {
            validateAdmin(account);
        } else if (account instanceof Journalist) {
            validateJournalist(account);
        } else if (account instanceof User) {
            validateUser(account);
        }
    }

    public void isJournalistValid(AccountEntity account) {
        isEmpty(account);
        isJournalist(account);
    }

    public void isUserValid(AccountEntity account) {
        isEmpty(account);
        isUser(account);
    }

    private void validateAccountFields(Account account) {
        // Validate fields
        isFullNameLengthValid(account.getFullName());
        isUsernameLengthValid(account.getUsername());
        isEmailFormatValid(account.getEmail());
        isDateOfBirthValid(account.getDateOfBirth());
    }

    private void validateAdmin(Account account) {
        adminValidation.checkAdmin(account);
    }

    private void validateJournalist(Account account) {
        journalistValidation.checkJournalist(account);
    }

    private void validateUser(Account account) {
        userValidation.checkUser(account);
    }


    public void existsByUsername(String username) {
        if(repo.existsByUsername(username)) {
            throw new AccountExistsException("Account already exists");
        }
    }

    public void isEmpty(Account account) {
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public void isEmpty(AccountEntity accountEntity) {
        if(accountEntity == null) {
            throw new AccountNotFoundException("Account not found");
        }
    }

    private void isJournalist(AccountEntity account) {
        if(!(account instanceof JournalistEntity)) {
            throw new JournalistNotFoundException("Journalist with id " + account.getId() + " not found");
        }
    }

    private void isUser(AccountEntity account) {
        if(!(account instanceof UserEntity)) {
            throw new UserNotFoundException("User with id " + account.getId() + " not found");
        }
    }

    private void isAccountTypeValid(Account account) {
        if(!(account instanceof Admin) && !(account instanceof Journalist) && !(account instanceof User)) {
            throw new UnsupportedAccountTypeException("Unsupported account type");
        }
    }

    private void isDateOfBirthValid(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        if (dateOfBirth.isBefore(LocalDate.of(1900, 1, 1)) || dateOfBirth.isAfter(currentDate)) {
            throw new InvalidFieldFormatException("Date of birth " + dateOfBirth + " is out of bounds");
        }
    }

    private void isFullNameLengthValid(String fullName) {
        if(fullName != null) {
            if(fullName.length() < 2 || fullName.length() > 50) {
                throw new InvalidFieldFormatException("Invalid full name length");
            }
        } else {
            throw new MissingFieldException("Full name not found");
        }
    }

    private void isUsernameLengthValid(String username) {
        if(username != null) {
            if (username.length() < 2 || username.length() > 50) {
                throw new InvalidFieldFormatException("Invalid username length");
            }
        } else {
            throw new MissingFieldException("Username not found");
        }
    }

    private void isEmailFormatValid(String email) {
        if(email != null) {
            String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            // Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(emailRegex);
            // Create instance of matcher
            Matcher matcher = pattern.matcher(email);
            // Check if email does not match the required format
            if(!matcher.matches()) {
                throw new InvalidFieldFormatException("Invalid email format");
            }
        } else {
            throw new MissingFieldException("Email not found");
        }
    }

    public void isPasswordLengthSufficient(String password) {
        if(password != null) {
            if (password.length() < 8 || password.length() > 200) {
                throw new InvalidFieldFormatException("Password is too short. It should be at least 8 characters long");
            }
        } else {
            throw new MissingFieldException("Password not found");
        }
    }
}
