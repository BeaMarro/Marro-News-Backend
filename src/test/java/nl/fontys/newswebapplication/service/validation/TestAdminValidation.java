package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.validation.AdminValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAdminValidation {
    private AccountRepository repo;
    private final AdminValidation adminValidation = new AdminValidation(repo);
    private Admin admin;

    @BeforeEach
    void setup() {
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        admin = Admin.builder()
                .id(3)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake1234.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 10))
                .profilePicture(Arrays.toString(profilePicture))
                .password("JakeDoe1234")
                .company("Fontys")
                .build();
    }

    @Test
    void testValidCompanyIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> adminValidation.checkAdmin(admin));
    }

    @Test
    void testEmptyCompanyIsValid_shouldThrowException() {
        // Arrange
        admin.setCompany(null);
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            adminValidation.checkAdmin(admin);
        });
    }
}
