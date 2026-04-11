package nl.fontys.newswebapplication.services.validation;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.request.ArticleRequest;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.repositories.ArticleRepository;
import nl.fontys.newswebapplication.repositories.entity.ArticleEntity;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleExistsException;
import nl.fontys.newswebapplication.services.exceptions.article.ArticleNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class ArticleValidation {
    private final ArticleRepository articleRepository;

    public void existsByHeading(String heading) {
        if(articleRepository.existsByHeading(heading)) {
            throw new ArticleExistsException("Article already exists");
        }
    }

    public void isEmpty(Article article) {
        if(article == null) {
            throw new ArticleNotFoundException("Article is empty");
        }
    }

    public void isEmpty(ArticleRequest articleRequest) {
        if(articleRequest == null) {
            throw new ArticleNotFoundException("Article not found");
        }
    }

    public void isEmpty(ArticleEntity articleEntity) {
        if (articleEntity == null) {
            throw new ArticleNotFoundException("Article not found");
        }
    }

    public void isHeadingLengthValid(String heading) {
        if(heading != null) {
            if(heading.length() < 2) {
                throw new InvalidFieldFormatException("Invalid heading length");
            }
        } else {
            throw new MissingFieldException("Heading not found");
        }
    }

    public void isTextLengthValid(String text) {
        if(text != null) {
            if(text.length() < 50) {
                throw new InvalidFieldFormatException("Invalid article text length");
            }
        } else {
            throw new MissingFieldException("Article Text not found");
        }
    }

    public void isGenreValid(int id) {
        if(!Genre.exists(id)) {
            throw new MissingFieldException("Genre with id " + id + " not found");
        }
    }

    public void isVideoLinkValid(Optional<String> videoLink) {
        if(videoLink.isPresent()) {
            String video = videoLink.get();
            // Check that video link matches the format of a YouTube embed
            String regex = "https://www\\.youtube\\.com/embed/([a-zA-Z0-9_-]+)(?:\\?si=[a-zA-Z0-9_-]+)?";
            // Compile the pattern
            Pattern pattern = Pattern.compile(regex);
            // Check whether the video matches the format
            Matcher matcher = pattern.matcher(video);

            if (!matcher.matches()) {
                throw new InvalidFieldFormatException("Invalid video link format");
            }
        }
    }

    public void isCoverImageValid(String coverImage) {
        if (coverImage == null) {
            throw new MissingFieldException("Cover image not found");
        }
    }
}
