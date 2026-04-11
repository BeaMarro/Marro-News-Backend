package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.config.security.token.AccessTokenEncoder;
import nl.fontys.newswebapplication.config.security.token.impl.AccessTokenImpl;
import nl.fontys.newswebapplication.domain.response.LoginResponse;
import nl.fontys.newswebapplication.domain.request.LoginRequest;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.account.InvalidAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.login.InvalidCredentialsException;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        AccountEntity account = accountRepository.getAccountEntityByUsername(loginRequest.getUsername());
        if (account == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(account);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(AccountEntity account) {
        int accountId = account.getId();
        String role;

        if(account instanceof JournalistEntity) {
            role = "JOURNALIST";
        } else if(account instanceof AdminEntity) {
            role = "ADMIN";
        } else if(account instanceof UserEntity) {
            role = "USER";
        } else {
            throw new InvalidAccountTypeException("Invalid Account");
        }

        return accessTokenEncoder.encode(
                new AccessTokenImpl(account.getUsername(), accountId, role));
    }
}
