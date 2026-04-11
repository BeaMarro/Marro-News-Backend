package nl.fontys.newswebapplication.repositories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Generated
@Entity
@Table(name = "Admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminEntity extends AccountEntity {
    @NotNull
    @Column(name = "company")
    private String company;

    @JsonIgnore
    @OneToMany(mappedBy = "authorEntity", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<ArticleEntity> articles;
}
