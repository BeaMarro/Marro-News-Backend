package nl.fontys.newswebapplication.domain;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.fontys.newswebapplication.domain.enums.Department;

@Generated
@Getter
@Setter
@SuperBuilder // Used to include superclass fields (base class)
@NoArgsConstructor
public class Journalist extends Account {
    private Department department;
}

