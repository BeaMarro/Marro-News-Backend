package nl.fontys.newswebapplication.domain.response;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.fontys.newswebapplication.domain.enums.Department;

@Generated
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class JournalistResponse extends AccountResponse {
    private Department department;
}
