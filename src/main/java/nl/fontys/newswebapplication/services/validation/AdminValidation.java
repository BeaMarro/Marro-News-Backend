package nl.fontys.newswebapplication.services.validation;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import org.springframework.stereotype.Component;

@Component
public class AdminValidation extends AccountValidation {
    public AdminValidation(AccountRepository repo) {
        super(repo);
    }

    public void checkAdmin(Account account) {
        Admin admin = (Admin) account;
        isCompanyValid(admin.getCompany());
    }

    private void isCompanyValid(String company) {
        if (company == null) {
            throw new MissingFieldException("Company not found");
        }
    }
}
