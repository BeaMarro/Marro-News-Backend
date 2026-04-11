package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.repositories.Filter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class FilterAccountService implements Filter<AccountResponse> {
    @Override
    public AccountResponse[] useFilter(AccountResponse[] data, String filter) {
        List<AccountResponse> filteredAccounts = new ArrayList<>();

        for (AccountResponse account : data) {
            String regex = "\\b" + filter;
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher matcherUsername = pattern.matcher(account.getUsername());
            Matcher matcherFullName = pattern.matcher(account.getFullName());
            Matcher matcherEmail = pattern.matcher(account.getEmail());

            if (matcherUsername.find() || matcherFullName.find() || matcherEmail.find()) {
                filteredAccounts.add(account);
            }
        }

        return filteredAccounts.toArray(new AccountResponse[0]);
    }
}
