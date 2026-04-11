package nl.fontys.newswebapplication.services;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.repositories.Filter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class FilterArticlesService implements Filter<Article> {
    @Override
    public Article[] useFilter(Article[] data, String filter) {
        List<Article> filteredArticles = new ArrayList<>();

        for (Article article : data) {
            String regex = "\\b" + filter;
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher matcherHeading = pattern.matcher(article.getHeading());
            Matcher matcherText = pattern.matcher(article.getText());

            if (matcherHeading.find() || matcherText.find()) {
                filteredArticles.add(article);
            }
        }

        return filteredArticles.toArray(new Article[0]);
    }
}
