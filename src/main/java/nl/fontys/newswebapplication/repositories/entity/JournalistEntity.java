package nl.fontys.newswebapplication.repositories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Generated
@Entity
@Table(name = "Journalist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JournalistEntity extends AccountEntity {
    @NotNull
    @Column(name = "department_id")
    private int departmentId;

    @JsonIgnore
    @OneToMany(mappedBy = "authorEntity", fetch = FetchType.EAGER)
    private List<ArticleEntity> articles;
}
