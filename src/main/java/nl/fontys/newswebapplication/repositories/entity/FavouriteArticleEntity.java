package nl.fontys.newswebapplication.repositories.entity;

import jakarta.persistence.*;
import lombok.*;

@Generated
@Entity
@Table(name = "Favourites_List")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FavouriteArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favourites_list_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;
}
