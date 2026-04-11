package nl.fontys.newswebapplication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.domain.enums.Genre;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Article {
    private int id;
    private String heading;
    private String text;
    private LocalDate publishDate;
    private JournalistResponse author;
    private Genre genre;
    private String coverImage;
    private Optional<String> video;
    private ApprovalStatus status;
}
