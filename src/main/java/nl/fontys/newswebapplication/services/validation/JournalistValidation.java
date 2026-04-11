package nl.fontys.newswebapplication.services.validation;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.stereotype.Component;

@Component
public class JournalistValidation extends AccountValidation {
    public JournalistValidation(AccountRepository repo) {
        super(repo);
    }

    public void checkJournalist(Account account) {
        Journalist journalist = (Journalist) account;
        isDepartmentValid(journalist.getDepartment().ordinal());
    }

    private void isDepartmentValid(int id) {
        if (!Department.exists(id)) {
            throw new MissingFieldException("Department with id " + id + " not found");
        }
    }
}
