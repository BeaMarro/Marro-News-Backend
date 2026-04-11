package nl.fontys.newswebapplication.service.validation;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.account.AccountExistsException;
import nl.fontys.newswebapplication.services.exceptions.account.AccountNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.account.journalist.JournalistNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.account.user.UserNotFoundException;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.exceptions.field.MissingFieldException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import nl.fontys.newswebapplication.services.validation.AdminValidation;
import nl.fontys.newswebapplication.services.validation.JournalistValidation;
import nl.fontys.newswebapplication.services.validation.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestAccountValidation {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserValidation userValidation;
    @Mock
    private AdminValidation adminValidation;
    @Mock
    private JournalistValidation journalistValidation;
    @InjectMocks
    private AccountValidation accountValidation;

    private Account account;
    private Account emptyAccount;
    private Account invalidAccount;
    private Journalist journalist;
    private Admin admin;
    private User user;
    private UserEntity userEntity;
    private JournalistEntity journalistEntity;
    private AdminEntity adminEntity;
    private UserEntity emptyAccountEntity;

    @BeforeEach
    void setup() {
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 8, 9));
        userEntity.setProfilePicture(profilePicture);
        userEntity.setPassword("BMarro1234");
        userEntity.setBio("test");

        journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@marronews.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1995, 8, 9));
        journalistEntity.setProfilePicture(profilePicture);
        journalistEntity.setPassword("JDoe1234");
        journalistEntity.setDepartmentId(Department.EDITORIAL.ordinal());

        adminEntity = new AdminEntity();
        adminEntity.setId(3);
        adminEntity.setFullName("Jack eDoe");
        adminEntity.setUsername("Jack.Doe");
        adminEntity.setEmail("jack.doe@marronews.com");
        adminEntity.setDateOfBirth(LocalDate.of(1980, 7, 3));
        adminEntity.setProfilePicture(profilePicture);
        adminEntity.setPassword("JackDoe1234");
        adminEntity.setCompany("Marro News");

        account = User.builder()
                .id(2)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture("bea_marro.jpg")
                .password("BMarro1234")
                .bio("test")
                .build();

        emptyAccount = null;

        invalidAccount = Account.builder()
                .id(3)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture("bea_marro.jpg")
                .password("BMarro1234")
                .build();

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

        emptyAccountEntity = null;

        // Admin
        admin = Admin.builder()
                .id(3)
                .fullName("Jane Doe")
                .username("Jane.Doe")
                .email("jane.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 10))
                .profilePicture(Arrays.toString(profilePicture))
                .password("JaneDoe1234")
                .company("Fontys")
                .build();

        // User
        user = User.builder()
                .id(2)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture(Arrays.toString(profilePicture))
                .password("BMarro1234")
                .bio("test")
                .build();
    }

    @Test
    void testValidPasswordIsEmpty_shouldReturnNothing() {
        // Arrange
        String password = "B181932#@1839";
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isPasswordLengthSufficient(password));
    }

    @Test
    void testEmptyPasswordIsEmpty_shouldThrowException() {
        // Arrange
        String password = null;
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            accountValidation.isPasswordLengthSufficient(password);
        });
    }

    @Test
    void testValidPasswordLengthIsValid_shouldReturnNothing() {
        // Arrange
        String password = userEntity.getPassword();

        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isPasswordLengthSufficient(password));
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidPasswordIsSupplied")
    void testInvalidPasswordLengthIsValid_shouldThrowException(String password) {
        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () -> {
            accountValidation.isPasswordLengthSufficient(password);
        });
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidPasswordIsSupplied() {
        return Stream.of(
                Arguments.of("1"), // Password length too short
                Arguments.of("xY#p2!aFwLr8ZgNqD9sHcTmE5vXuKoI7bR3iU1eJ4oG6nA0yS8WpC2hQfMzVxYrEoKtHqDwBmFjZlVxC7GkL2O9P3N6aHtR7XpS9oL6T1P8aJ0yS3L2W4nB5eG7nH8sJ9rI5fT6yU4oP1wC3oI8rN5zA9l8dR1l7K0zP3kG2cV4jM9bB8zX1dC5vB6eJ3nB9rI2yS7tN8oD1zH0") // Password length too long
        );
    }

    @Test
    void testValidDateOfBirthIsValid_shouldReturnNothing() {
        // Arrange
        LocalDate dateOfBirth = LocalDate.of(2005, 7, 9);
        account.setDateOfBirth(dateOfBirth);
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(account));
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidDatesAreSupplied")
    void testInvalidDateOfBirthIsValid_shouldThrowException(LocalDate dateOfBirth) {
        // Arrange
        account.setDateOfBirth(dateOfBirth);
        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidDatesAreSupplied() {
        return Stream.of(
                Arguments.of(LocalDate.of(1880,2,1)), // Date in extreme past
                Arguments.of(LocalDate.of(2055,3,4)) // Date in extreme future
        );
    }

    @Test
    void testValidAccountIsEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isEmpty(account));
    }

    @Test
    void testEmptyAccountIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountValidation.isEmpty(emptyAccount);
        });
    }

    @Test
    void testValidAccountEntityIsEmpty_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isEmpty(userEntity));
    }

    @Test
    void testEmptyAccountEntityIsEmpty_shouldThrowException() {
        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountValidation.isEmpty(emptyAccountEntity);
        });
    }

    @Test
    void testExistsByNonExistingUsername_shouldReturnNothing() {
        // Assert
        String invalidUsername = "Fontys";
        when(accountRepository.existsByUsername(invalidUsername)).thenReturn(false);

        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.existsByUsername(invalidUsername));
    }

    @Test
    void testExistsByExistingUsername_shouldThrowException() {
        // Assert
        String validUsername = "B.Marro";
        when(accountRepository.existsByUsername(validUsername)).thenReturn(true);

        // Act and Assert
        assertThrows(AccountExistsException.class, () -> {
            accountValidation.existsByUsername(validUsername);
        });
    }

    @Test
    void testJournalistEntityIsJournalist_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isJournalistValid(journalistEntity));
    }

    @Test
    void testInvalidAccountEntityIsJournalist_shouldThrowException() {
        // Act and Assert
        assertThrows(JournalistNotFoundException.class, () -> {
            accountValidation.isJournalistValid(userEntity);
        });
    }

    @Test
    void testUserEntityIsUser_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.isUserValid(userEntity));
    }

    @Test
    void testInvalidAccountEntityIsUser_shouldThrowException() {
        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            accountValidation.isUserValid(journalistEntity);
        });
    }

    @Test
    void testValidFullNameIsEmpty_shouldReturnNothing() {
        // Arrange
        String validFullName = "Beatrice Marro";
        account.setFullName(validFullName);
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(account));
    }

    @Test
    void testEmptyFullNameIsEmpty_shouldThrowException() {
        // Arrange
        account.setFullName(null);
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidFullNamesAreSupplied")
    void testInvalidFullNameLengthIsValid_shouldThrowException(String fullName) {
        // Arrange
        account.setFullName(fullName);
        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidFullNamesAreSupplied() {
        return Stream.of(
                Arguments.of("B"),
                Arguments.of("Eleanor Marianna Thornton-Preston Montgomery-Winters")
        );
    }

    @Test
    void testValidUsernameIsEmpty_shouldReturnNothing() {
        // Arrange
        String validUsername = "B.Marro";
        account.setUsername(validUsername);
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(account));
    }

    @Test
    void testEmptyUsernameIsEmpty_shouldThrowException() {
        // Arrange
        account.setUsername(null);
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidUsernamesAreSupplied")
    void testInvalidUsernameLengthIsValid_shouldThrowException(String username) {
        // Arrange
        account.setUsername(username);
        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidUsernamesAreSupplied() {
        return Stream.of(
                Arguments.of("B"),
                Arguments.of("TrustedNewsSourceWithInsightsAndAnalysisForInformedReadership2023")
        );
    }

    @Test
    void testValidEmailIsEmpty_shouldReturnNothing() {
        // Arrange
        String validEmail = "b.marro@marronews.it";
        account.setEmail(validEmail);
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(account));
    }

    @Test
    void testEmptyEmailIsEmpty_shouldThrowException() {
        // Arrange
        account.setEmail(null);
        // Act and Assert
        assertThrows(MissingFieldException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidEmailsAreSupplied")
    void testInvalidEmailIsValid_shouldThrowException(String email) {
        // Arrange
        account.setEmail(email);
        // Act and Assert
        assertThrows(InvalidFieldFormatException.class, () -> {
            accountValidation.validateAccount(account);
        });
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidEmailsAreSupplied() {
        return Stream.of(
                Arguments.of("b.marro@marronews@it"),
                Arguments.of("b.marro@ marronews.it"),
                Arguments.of("Beatrice Marro"),
                Arguments.of("b.marro@")
        );
    }

    @Test
    void testValidUserAccountTypeIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(user));
    }

    @Test
    void testValidJournalistAccountTypeIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(journalist));
    }

    @Test
    void testValidAdminAccountTypeIsValid_shouldReturnNothing() {
        // Act and Assert
        assertDoesNotThrow(() -> accountValidation.validateAccount(admin));
    }

    @Test
    void testUnsupportedAccountTypeIsValid_shouldThrowException() {
        assertThrows(UnsupportedAccountTypeException.class, () -> {
            accountValidation.validateAccount(invalidAccount);
        });
    }
}
