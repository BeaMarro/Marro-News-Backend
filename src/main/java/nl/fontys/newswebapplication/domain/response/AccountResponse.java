package nl.fontys.newswebapplication.domain.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Generated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountResponse {
    private int id;
    private String fullName;
    private String username;
    private LocalDate dateOfBirth;
    private String email;
    private String profilePicture;
}
