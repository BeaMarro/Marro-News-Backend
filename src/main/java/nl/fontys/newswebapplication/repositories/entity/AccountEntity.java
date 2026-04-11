package nl.fontys.newswebapplication.repositories.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Generated
@Entity
@Table(name = "Account")
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int id;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(name = "full_name")
    private String fullName;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(name = "username")
    private String username;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Email
    @Size(max = 200)
    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @NotBlank
    @Size(min = 8, max = 200)
    @Column(name = "password")
    private String password;
}
