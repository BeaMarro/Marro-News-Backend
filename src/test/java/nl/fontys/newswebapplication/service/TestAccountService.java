package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.AdminResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.domain.response.UserResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.AccountService;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.exceptions.account.*;
import nl.fontys.newswebapplication.services.exceptions.field.InvalidFieldFormatException;
import nl.fontys.newswebapplication.services.validation.AccountValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestAccountService {
    @Mock
    private AccountRepository accountRepositoryMock;
    @Mock
    private AccountConverter accountConverter;
    @Mock
    private AccountValidation accountValidation;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountService accountService;

    private JournalistEntity journalistEntity;
    private UserEntity userEntity;
    private AdminEntity adminEntity;
    private AccountEntity accountEntity;
    private AccountEntity invalidAccountEntity;

    private JournalistEntity updatedJournalistEntity;
    private UserEntity updatedUserEntity;
    private AdminEntity updatedAdminEntity;

    private Journalist journalist;
    private User user;
    private Admin admin;
    private Account account;
    private Account invalidAccount;

    private Journalist updatedJournalist;
    private User updatedUser;
    private Admin updatedAdmin;

    private String password;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        password = "test1234";
        encodedPassword = "Encoded Password";
        String profilePicture = "profile_picture.jpg";
        String updatedProfilePicture = "profile_picture_2.jpg";
        byte[] profilePictureBlob = profilePicture.getBytes();
        byte[] updatedProfilePictureBlob = updatedProfilePicture.getBytes();

        // Journalist Entity
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePictureBlob);
        journalistEntity.setPassword(encodedPassword);
        journalistEntity.setDepartmentId(2);

        updatedJournalistEntity = new JournalistEntity();
        updatedJournalistEntity.setId(1);
        updatedJournalistEntity.setFullName("John Doe 2");
        updatedJournalistEntity.setUsername("J.Doe 2");
        updatedJournalistEntity.setEmail("john.doe@news.com");
        updatedJournalistEntity.setDateOfBirth(LocalDate.of(1985, 7, 9));
        updatedJournalistEntity.setProfilePicture(updatedProfilePictureBlob);
        updatedJournalistEntity.setPassword(encodedPassword);
        updatedJournalistEntity.setDepartmentId(Department.LAYOUT_DESIGN.ordinal());

        // User Entity
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 8, 9));
        userEntity.setProfilePicture(profilePictureBlob);
        userEntity.setPassword(encodedPassword);
        userEntity.setBio("test");

        updatedUserEntity  = new UserEntity();
        updatedUserEntity.setId(2);
        updatedUserEntity.setFullName("Beatrice Marro 2");
        updatedUserEntity.setUsername("B.Marro 2");
        updatedUserEntity.setEmail("beatrice.marro@student.fontys.nl");
        updatedUserEntity.setDateOfBirth(LocalDate.of(2005, 7, 9));
        updatedUserEntity.setProfilePicture(updatedProfilePictureBlob);
        updatedUserEntity.setPassword(encodedPassword);
        updatedUserEntity.setBio("testing Marro News");

        // Admin Entity
        adminEntity = new AdminEntity();
        adminEntity.setId(3);
        adminEntity.setFullName("Jane Doe");
        adminEntity.setUsername("Jane.Doe");
        adminEntity.setEmail("jane.doe@news.nl");
        adminEntity.setDateOfBirth(LocalDate.of(1995, 6, 10));
        adminEntity.setProfilePicture(profilePictureBlob);
        adminEntity.setPassword(encodedPassword);
        adminEntity.setCompany("Fontys");

        updatedAdminEntity = new AdminEntity();
        updatedAdminEntity.setId(3);
        updatedAdminEntity.setFullName("Janet Doe");
        updatedAdminEntity.setUsername("Janet.Doe");
        updatedAdminEntity.setEmail("janet.doe@news.nl");
        updatedAdminEntity.setDateOfBirth(LocalDate.of(1995, 6, 9));
        updatedAdminEntity.setProfilePicture(updatedProfilePictureBlob);
        updatedAdminEntity.setPassword(encodedPassword);
        updatedAdminEntity.setCompany("Marro News Ltd");

        // Account Entity
        accountEntity = AccountEntity.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture(profilePictureBlob)
                .password("JakeDoe1234")
                .build();

        invalidAccountEntity = AccountEntity.builder()
                .id(999)
                .build();

        // Journalist
        journalist = Journalist.builder()
                .id(1)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture(profilePicture)
                .password("JakeDoe21234")
                .department(Department.COPYRIGHTING)
                .build();

        updatedJournalist = Journalist.builder()
                .id(1)
                .fullName("John Doe 2")
                .username("J.Doe 2")
                .email("john.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 7, 9))
                .profilePicture(updatedProfilePicture)
                .password(encodedPassword)
                .department(Department.LAYOUT_DESIGN)
                .build();

        // User
        user = User.builder()
                .id(2)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture(profilePicture)
                .password("BMarro1234")
                .bio("test")
                .build();

        updatedUser = User.builder()
                .id(2)
                .fullName("Beatrice Marro 2")
                .username("B.Marro 2")
                .email("beatrice.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 7, 9))
                .profilePicture(updatedProfilePicture)
                .password(encodedPassword)
                .bio("testing Marro News")
                .build();

        // Admin
        admin = Admin.builder()
                .id(3)
                .fullName("Jane Doe")
                .username("Jane.Doe")
                .email("jane.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 10))
                .profilePicture(profilePicture)
                .password("JaneDoe1234")
                .company("Fontys")
                .build();

        updatedAdmin = Admin.builder()
                .id(3)
                .fullName("Janet Doe")
                .username("Janet.Doe")
                .email("janet.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 9))
                .profilePicture(updatedProfilePicture)
                .password(encodedPassword)
                .company("Marro News Ltd")
                .build();

        // Account
        account = Account.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture("jake_doe.jpg")
                .password("JakeDoe1234")
                .build();

        invalidAccount = Account.builder()
                .id(999)
                .build();
    }

    @Test
    void getAllAccounts_shouldReturnAllAccountsConverted() {
        // Arrange
        List<AccountEntity> expectedEntities = List.of(journalistEntity, userEntity);
        List<Account> expected = List.of(journalist, user);
        when(accountRepositoryMock.findAll()).thenReturn(expectedEntities);
        // Act
        List<AccountResponse> actual = accountService.getAll();
        // Assert
        assertEquals(expected.size(), actual.size());
        verify(accountRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetJournalistById_shouldReturnJournalistConverted() {
        journalist.setPassword(encodedPassword);
        Journalist expectedResult = journalist;

        when(accountRepositoryMock.getAccountEntityById(1)).thenReturn(journalistEntity);

        JournalistResponse actualResult = (JournalistResponse) accountService.getById(1);

        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getDepartment(), actualResult.getDepartment());

        verify(accountRepositoryMock).getAccountEntityById(1);
    }

    @Test
    void testGetUserById_shouldReturnUserConverted() {
        user.setPassword(encodedPassword);
        User expectedResult = user;

        when(accountRepositoryMock.getAccountEntityById(1)).thenReturn(userEntity);
        UserResponse actualResult = (UserResponse) accountService.getById(1);

        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getBio(), actualResult.getBio());

        verify(accountRepositoryMock).getAccountEntityById(1);
    }

    @Test
    void testGetAdminById_shouldReturnAdminConverted() {
        admin.setPassword(encodedPassword);
        Admin expectedResult = admin;

        when(accountRepositoryMock.getAccountEntityById(1)).thenReturn(adminEntity);

        AdminResponse actualResult = (AdminResponse) accountService.getById(1);


        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getCompany(), actualResult.getCompany());

        verify(accountRepositoryMock).getAccountEntityById(1);
    }

    @Test
    void testGetAccountByInvalidId_shouldThrowException() {
        int invalidAccountId = 999;

        when(accountRepositoryMock.getAccountEntityById(invalidAccountId)).thenReturn(invalidAccountEntity);
        doThrow(new AccountNotFoundException()).when(accountValidation).isEmpty(invalidAccountEntity);

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getById(invalidAccountId);
        });
    }

    @Test
    void testGetUnsupportedAccountTypeById_shouldThrowException() {
        // Arrange
        int id = account.getId();
        when(accountRepositoryMock.getAccountEntityById(id)).thenReturn(accountEntity);

        // Act and Assert
        assertThrows(UnsupportedAccountTypeException.class, () -> {
            accountService.getById(id);
        });

        verify(accountRepositoryMock).getAccountEntityById(id);

    }

    @Test
    void testCreateJournalist_shouldReturnJournalistConverted() {
        Journalist expectedResult = journalist;
        String password = expectedResult.getPassword();

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.save(journalistEntity)).thenReturn(journalistEntity);

        Journalist actualResult = (Journalist) accountService.save(expectedResult);

        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getPassword(), actualResult.getPassword());
        assertEquals(expectedResult.getDepartment(), actualResult.getDepartment());

        verify(accountRepositoryMock).save(journalistEntity);
    }

    @Test
    void testCreateUser_shouldReturnUserConverted() {
        User expectedResult = user;
        String password = expectedResult.getPassword();

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.save(userEntity)).thenReturn(userEntity);


        User actualResult = (User) accountService.save(expectedResult);

        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getPassword(), actualResult.getPassword());
        assertEquals(expectedResult.getBio(), actualResult.getBio());

        verify(accountRepositoryMock).save(userEntity);
    }

    @Test
    void testCreateAdmin_shouldReturnAdminConverted() {
        Admin expectedResult = admin;
        String password = expectedResult.getPassword();

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.save(adminEntity)).thenReturn(adminEntity);

        Admin actualResult = (Admin) accountService.save(expectedResult);

        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getPassword(), actualResult.getPassword());
        assertEquals(expectedResult.getCompany(), actualResult.getCompany());

        verify(accountRepositoryMock).save(adminEntity);
    }

    @Test
    void testCreateExistingAccount_shouldThrowException() {
        // Arrange
        doThrow(new AccountNotFoundException()).when(accountValidation).existsByUsername(journalistEntity.getUsername());

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.save(journalist);
        });
    }

    @Test
    void testDeleteAccount_shouldReturnNothing() {
        // Arrange
        int id = journalistEntity.getId();

        // Act
        when(accountRepositoryMock.getAccountEntityById(id)).thenReturn(journalistEntity);
        accountService.delete(id);

        // Assert
        verify(accountRepositoryMock).getAccountEntityById(id);
        verify(accountRepositoryMock).deleteById(id);
    }

    @Test
    void testDeleteByInvalidAccount_shouldThrowException() {
        // Arrange
        int invalidId = 100;
        when(accountRepositoryMock.getAccountEntityById(invalidId)).thenReturn(invalidAccountEntity);
        doThrow(new AccountNotFoundException()).when(accountValidation).isEmpty(invalidAccountEntity);

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.delete(invalidId);
        });

        verify(accountRepositoryMock).getAccountEntityById(invalidId);
    }

    @Test
    void testUpdateJournalist_shouldReturnJournalistConverted() {
        // Arrange
        Journalist expectedResult = updatedJournalist;
        expectedResult.setPassword(null);

        when(accountRepositoryMock.getAccountEntityById(updatedJournalistEntity.getId())).thenReturn(updatedJournalistEntity);
        when(accountRepositoryMock.save(updatedJournalistEntity)).thenReturn(updatedJournalistEntity); // Update

        // Act
        accountService.update(updatedJournalist);
        JournalistResponse actualResult = (JournalistResponse) accountService.getById(updatedJournalistEntity.getId());

        // Assert
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getDepartment(), actualResult.getDepartment());

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedJournalist.getId());
        verify(accountRepositoryMock).save(updatedJournalistEntity);
    }

    @Test
    void testUpdateUser_shouldReturnUserConverted() {
        // Arrange
        User expectedResult = updatedUser;
        expectedResult.setPassword(null);

        when(accountRepositoryMock.getAccountEntityById(updatedUserEntity.getId())).thenReturn(updatedUserEntity);
        when(accountRepositoryMock.save(updatedUserEntity)).thenReturn(updatedUserEntity); // Update

        // Act
        accountService.update(updatedUser);
        UserResponse actualResult = (UserResponse) accountService.getById(updatedUserEntity.getId());

        // Assert
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getBio(), actualResult.getBio());

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedUser.getId());
        verify(accountRepositoryMock).save(updatedUserEntity);
    }

    @Test
    void testUpdateAdmin_shouldReturnAdminConverted() {
        // Arrange
        Admin expectedResult = updatedAdmin;
        expectedResult.setPassword(null);

        when(accountRepositoryMock.getAccountEntityById(updatedAdminEntity.getId())).thenReturn(updatedAdminEntity);
        when(accountRepositoryMock.save(updatedAdminEntity)).thenReturn(updatedAdminEntity); // Update

        // Act
        accountService.update(updatedAdmin);
        AdminResponse actualResult = (AdminResponse) accountService.getById(updatedAdminEntity.getId());

        // Assert
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        assertEquals(expectedResult.getFullName(), actualResult.getFullName());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getDateOfBirth(), actualResult.getDateOfBirth());
        assertEquals(expectedResult.getProfilePicture(), actualResult.getProfilePicture());
        assertEquals(expectedResult.getCompany(), actualResult.getCompany());

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedAdmin.getId());
        verify(accountRepositoryMock).save(updatedAdminEntity);
    }

    @Test
    void testUpdateAdminPassword_shouldReturnAdminConverted() {
        // Arrange
        Admin expectedResult = updatedAdmin;
        expectedResult.setPassword(password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.getAccountEntityById(updatedAdminEntity.getId())).thenReturn(updatedAdminEntity);
        when(accountRepositoryMock.save(updatedAdminEntity)).thenReturn(updatedAdminEntity); // Update

        // Act
        accountService.update(updatedAdmin);
        String actualResult = accountRepositoryMock.getAccountEntityById(expectedResult.getId()).getPassword();

        // Assert
        assertEquals(expectedResult.getPassword(), actualResult);;

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedAdmin.getId());
        verify(accountRepositoryMock).save(updatedAdminEntity);
    }

    @Test
    void testUpdateJournalistPassword_shouldReturnJournalistConverted() {
        // Arrange
        Journalist expectedResult = updatedJournalist;
        expectedResult.setPassword(password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.getAccountEntityById(updatedJournalistEntity.getId())).thenReturn(updatedJournalistEntity);
        when(accountRepositoryMock.save(updatedJournalistEntity)).thenReturn(updatedJournalistEntity); // Update

        // Act
        accountService.update(updatedJournalist);
        String actualResult = accountRepositoryMock.getAccountEntityById(expectedResult.getId()).getPassword();

        // Assert
        assertEquals(expectedResult.getPassword(), actualResult);;

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedJournalist.getId());
        verify(accountRepositoryMock).save(updatedJournalistEntity);
    }

    @Test
    void testUpdateUserPassword_shouldReturnUserConverted() {
        // Arrange
        User expectedResult = updatedUser;
        expectedResult.setPassword(password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.getAccountEntityById(updatedUserEntity.getId())).thenReturn(updatedUserEntity);
        when(accountRepositoryMock.save(updatedUserEntity)).thenReturn(updatedUserEntity); // Update

        // Act
        accountService.update(updatedUser);
        String actualResult = accountRepositoryMock.getAccountEntityById(expectedResult.getId()).getPassword();

        // Assert
        assertEquals(expectedResult.getPassword(), actualResult);

        verify(accountRepositoryMock, times(2)).getAccountEntityById(updatedUser.getId());
        verify(accountRepositoryMock).save(updatedUserEntity);
    }

//    @Test
//    void testUpdateNonExistingAccount_shouldThrowException() {
//        int id = admin.getId();
//
//        when(accountRepositoryMock.getAccountEntityById(id)).thenReturn(invalidAccountEntity);
//        doThrow(new AccountNotFoundException()).when(accountValidation).validateAccount(invalidAccount);
//
//        assertThrows(AccountNotFoundException.class, () -> {
//            accountService.update(admin);
//        });
//
//        verify(accountRepositoryMock).getAccountEntityById(id);
//    }

    @Test
    void testUpdateAccountByUnsupportedAccountType_shouldThrowException() {
        int id = account.getId();

        when(accountRepositoryMock.getAccountEntityById(id)).thenReturn(accountEntity);

        assertThrows(UnsupportedAccountTypeException.class, () -> {
            accountService.update(account);
        });

        verify(accountRepositoryMock).getAccountEntityById(id);
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidPasswordsAreSupplied")
    void testCreateAccountByInsufficientPasswordLength_shouldThrowException(String password) {
        journalistEntity.setPassword(password);
        journalist.setPassword(password);

        when(passwordEncoder.encode(password)).thenReturn(password);
        when(accountRepositoryMock.save(journalistEntity)).thenThrow(new InvalidFieldFormatException("Password is too short. It should be at least 8 characters long"));

        assertThrows(InvalidFieldFormatException.class, () -> {
            accountService.save(journalist);
        });

        verify(accountRepositoryMock).save(journalistEntity);
    }

    @ParameterizedTest
    @MethodSource("provideForCreate_shouldThrowException_whenInvalidDatesAreSupplied")
    void testCreateAccountWithExtremePastDateOfBirth_shouldThrowException(LocalDate dateOfBirth) {
        String password = journalist.getPassword();
        journalistEntity.setDateOfBirth(dateOfBirth);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accountRepositoryMock.save(journalistEntity)).thenThrow(new InvalidFieldFormatException("Date of Birth out of bounds."));

        journalist.setDateOfBirth(dateOfBirth);

        assertThrows(InvalidFieldFormatException.class, () -> {
            accountService.save(journalist);
        });

        verify(accountRepositoryMock).save(journalistEntity);
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidPasswordsAreSupplied() {
        return Stream.of(
                Arguments.of("1234"), // Password too short
                Arguments.of("M#r4o2N7eW8sP1aQ9sR0lO6sI3vE5N8eW1sO7mQ2xT4zL9fA3hY5m1D8pX6cU0tI4oN") // Password too long
        );
    }

    private static Stream<Arguments> provideForCreate_shouldThrowException_whenInvalidDatesAreSupplied() {
        return Stream.of(
                Arguments.of(LocalDate.of(1880,2,1)), // Date in extreme past
                Arguments.of(LocalDate.of(2055,3,4)) // Date in extreme future
        );
    }
}