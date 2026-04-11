package nl.fontys.newswebapplication.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.*;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository repo;
    private final AccountValidation accountValidation;
    private final PasswordEncoder passwordEncoder;

    public List<AccountResponse> getAll() {
        List<AccountResponse> accounts = new ArrayList<>();
        for(AccountEntity accountEntity : repo.findAll()) {
            AccountResponse account = AccountConverter.convertToAccountResponse(accountEntity);
            accounts.add(account);
        }
        return accounts;
    }

    public AccountResponse getById(int id) {
        AccountEntity accountEntity = repo.getAccountEntityById(id);
        accountValidation.isEmpty(accountEntity);

        return AccountConverter.convertToAccountResponse(accountEntity);
    }

    @Transactional
    public Account save(Account newAccount) {
        // Validate fields
        accountValidation.existsByUsername(newAccount.getUsername());
        accountValidation.validateAccount(newAccount);
        accountValidation.isPasswordLengthSufficient(newAccount.getPassword());

        // Encode Password
        String encodedPassword = passwordEncoder.encode(newAccount.getPassword());
        newAccount.setPassword(encodedPassword);

        AccountEntity accountEntity = AccountConverter.convertToAccountEntity(newAccount);
        AccountEntity savedAccountEntity = repo.save(accountEntity);
        return AccountConverter.convertToAccount(savedAccountEntity);
    }

    @Transactional
    public void delete(int id) {
        AccountEntity accountEntity = repo.getAccountEntityById(id);

        // Validate Account
        accountValidation.isEmpty(accountEntity);

        repo.deleteById(id);
    }

    @Transactional
    public void update(Account updatedAccount) {
        int id = updatedAccount.getId();
        AccountEntity accountEntity = repo.getAccountEntityById(id);

        // Validate Account
        accountValidation.validateAccount(updatedAccount);

        // Validate and Encode Password
        if(updatedAccount.getPassword() != null) { // Only update password if it is provided
            String encodedPassword = passwordEncoder.encode(updatedAccount.getPassword());
            updatedAccount.setPassword(encodedPassword);
        } else {
            updatedAccount.setPassword(accountEntity.getPassword());
        }

        AccountEntity updatedAccountEntity = AccountConverter.convertToAccountEntity(updatedAccount);
        repo.save(updatedAccountEntity);
    }
}
