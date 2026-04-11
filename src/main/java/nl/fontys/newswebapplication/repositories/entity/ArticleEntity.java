package nl.fontys.newswebapplication.repositories.entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Generated
@Entity
@Table(name = "Article")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private int id;

    @NotBlank
    @Length(min = 2 ,max = 500)
    @Column(name = "heading")
    private String heading;

    @NotBlank
    @Length(min = 2 ,max = 8000)
    @Column(name = "text")
    private String text;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private JournalistEntity authorEntity;

    @NotNull
    @Column(name = "genre_id")
    private int genreId;

    @Column(name = "video")
    private String videoLink;

    @NotNull
    @Column(name = "approval_status_id")
    private int approvalStatusId;

    @Lob
    @Column(name = "cover_image2", columnDefinition = "BLOB")
    private byte[] coverImage;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "favoriteArticles")
    private List<UserEntity> favoriteArticles;
}
