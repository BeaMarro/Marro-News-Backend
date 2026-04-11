package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.validation.JournalistValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestJournalistValidation {
    private AccountRepository repo;
    private final JournalistValidation journalistValidation = new JournalistValidation(repo);
    private Journalist journalist;

    @BeforeEach
    void setup() {
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Journalist
        journalist = Journalist.builder()
                .id(1)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture(Arrays.toString(profilePicture))
                .password("JakeDoe21234")
                .department(Department.COPYRIGHTING)
                .build();
    }

    @Test
    void testExistingDepartmentIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> journalistValidation.checkJournalist(journalist));
    }
}
