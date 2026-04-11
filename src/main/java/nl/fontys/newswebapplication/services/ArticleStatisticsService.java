package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ArticleStatisticsService {
    private final AccountRepository accountRepository;
    private final ArticleRepository articleRepository;

    public Map<String, Integer> getTotalArticlesByJournalists() {
        Map<String, Integer> totalArticlesByJournalist = new HashMap<>();

        List<JournalistEntity> journalists = getAllJournalists();

        for(JournalistEntity journalist : journalists) {
            int total = articleRepository.countTotalArticlesByJournalist(journalist.getId());
            String fullName = journalist.getFullName();
            totalArticlesByJournalist.put(fullName, total);
        }

        return totalArticlesByJournalist;
    }

    public Map<String, Double> getArticleShareByJournalists() {
        Map<String, Double> totalArticlesByJournalist = new HashMap<>();

        List<JournalistEntity> journalists = getAllJournalists();

        for(JournalistEntity journalist : journalists) {
            double share = articleRepository.findArticleShareByJournalist(journalist.getId());
            String fullName = journalist.getFullName();
            totalArticlesByJournalist.put(fullName, share);
        }

        return totalArticlesByJournalist;
    }

    private List<JournalistEntity> getAllJournalists() {
        List<AccountEntity> accounts = accountRepository.findAll();
        List<JournalistEntity> journalists = new ArrayList<>();

        for(AccountEntity account : accounts) {
            if(account instanceof JournalistEntity journalist) {
                journalists.add(journalist);
            }
        }
        return journalists;
    }
}
