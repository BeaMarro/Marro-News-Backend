package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.services.validation.ArticleValidation;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReadingTimeService {
    private ArticleRepository repo;
    private ArticleValidation validation;

    public int getReadingTimeByArticleId(int id) {
        // Time = Speed/Distance
        ArticleEntity article = repo.getArticleEntityById(id);

        validation.isEmpty(article);

        int totalWords = getTotalWords(article.getText());
        int wordsReadPerMinute = 200;

        // Calculate reading time and round up
        int readingTime = totalWords / wordsReadPerMinute;

        if(readingTime == 0) { // Round up if necessary
            readingTime = 1;
        }

        return readingTime;
    }

    private int getTotalWords(String text) {
        // Gets words by splitting text at empty characters (spaces/tabs)
        String[] words = text.split("\\s+");
        return words.length;
    }
}
