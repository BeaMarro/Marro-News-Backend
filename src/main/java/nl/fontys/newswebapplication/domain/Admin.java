package nl.fontys.newswebapplication.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Generated
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Admin extends Account {
    private String company;
}
