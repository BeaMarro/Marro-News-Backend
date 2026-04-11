package nl.fontys.newswebapplication.domain.response;

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
public class AdminResponse extends AccountResponse {
    private String company;
}
