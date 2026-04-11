package nl.fontys.newswebapplication.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}