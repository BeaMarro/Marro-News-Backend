package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.validation.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestUserValidation {
    private AccountRepository repo;
    private final UserValidation userValidation = new UserValidation(repo);
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(2);
        user.setFullName("Beatrice Marro");
        user.setUsername("B.Marro");
        user.setEmail("b.marro@student.fontys.nl");
        user.setDateOfBirth(LocalDate.of(2005, 8, 9));
        user.setProfilePicture("bea_marro.jpg");
        user.setPassword("BMarro1234");
        user.setBio("test");
    }

    @Test
    void testValidBioIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> userValidation.checkUser(user));
    }

    @Test
    void testEmptyBioIsValid_shouldThrowException() {
        // Arrange
        user.setBio(null);
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            userValidation.checkUser(user);
        });
    }
}
