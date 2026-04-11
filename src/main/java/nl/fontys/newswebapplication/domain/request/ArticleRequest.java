package nl.fontys.newswebapplication.domain.request;

import lombok.*;
import nl.fontys.newswebapplication.domain.enums.Genre;

import java.util.Optional;

@Generated
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ArticleRequest {
    private int id;
    private String heading;
    private String text;
    private int authorId;
    private Genre genre;
    private String coverImage;
    private Optional<String> video;
}
