package nl.fontys.newswebapplication.repositories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Generated
@Entity
@Table(name = "End_User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends AccountEntity {
    @NotNull
    @Length(min = 2 ,max = 500)
    @Column(name = "bio")
    private String bio;

    @JsonIgnore
    @OneToMany(mappedBy = "authorEntity", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<ArticleEntity> articles;

    // The many-to-many relationship for the favourites list is only present in the UserEntity as only users can exclusively access this feature
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Favourites_List",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<ArticleEntity> favoriteArticles;
}
