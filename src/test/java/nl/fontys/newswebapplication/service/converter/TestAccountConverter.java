package nl.fontys.newswebapplication.service.converter;

import nl.fontys.newswebapplication.domain.Account;
import nl.fontys.newswebapplication.domain.Admin;
import nl.fontys.newswebapplication.domain.Journalist;
import nl.fontys.newswebapplication.domain.User;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.converter.AccountConverter;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TestAccountConverter {
    @Mock
    private AccountConverter accountConverter;

    private AdminEntity adminEntity;
    private Admin admin;

    private JournalistEntity journalistEntity;
    private Journalist journalist;

    private UserEntity userEntity;
    private User user;

    private AccountEntity accountEntity;
    private Account account;

    @BeforeEach
    void setup() {
        String profilePicture = "profile_picture.jpg";
        byte[] profilePictureBlob = profilePicture.getBytes();

        // Journalist Entity
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePictureBlob);
        journalistEntity.setPassword("12345678910");
        journalistEntity.setDepartmentId(2);

        // User Entity
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 8, 9));
        userEntity.setProfilePicture(profilePictureBlob);
        userEntity.setPassword("Beatrice12345");
        userEntity.setBio("test");

        // Admin Entity
        adminEntity = new AdminEntity();
        adminEntity.setId(3);
        adminEntity.setFullName("Jane Doe");
        adminEntity.setUsername("Jane.Doe");
        adminEntity.setEmail("jane.doe@news.nl");
        adminEntity.setDateOfBirth(LocalDate.of(1995, 6, 10));
        adminEntity.setProfilePicture(profilePictureBlob);
        adminEntity.setPassword("Admin123123");
        adminEntity.setCompany("Fontys");

        // Account Entity
        accountEntity = AccountEntity.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture(profilePictureBlob)
                .password("12345678910")
                .build();

        // Journalist
        journalist = Journalist.builder()
                .id(1)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture(profilePicture)
                .password("12345678910")
                .department(Department.COPYRIGHTING)
                .build();

        // User
        user = User.builder()
                .id(2)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture(profilePicture)
                .password("Beatrice12345")
                .bio("test")
                .build();

        // Admin
        admin = Admin.builder()
                .id(3)
                .fullName("Jane Doe")
                .username("Jane.Doe")
                .email("jane.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 10))
                .profilePicture(profilePicture)
                .password("Admin123123")
                .company("Fontys")
                .build();

        // Account
        account = Account.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture(profilePicture)
                .password("12345678910")
                .build();
    }

    @Test
    void testConvertAdminEntityToAdmin_shouldReturnAdmin() {
        // Arrange
        Admin expected = admin;

        // Act
        Admin actual = (Admin) AccountConverter.convertToAccount(adminEntity);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getProfilePicture(), actual.getProfilePicture());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getCompany(), actual.getCompany());
    }

    @Test
    void testConvertJournalistEntityToJournalist_shouldReturnJournalist() {
        // Arrange
        Journalist expected = journalist;

        // Act
        Journalist actual = (Journalist) AccountConverter.convertToAccount(journalistEntity);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getProfilePicture(), actual.getProfilePicture());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getDepartment(), actual.getDepartment());
    }

    @Test
    void testConvertUserEntityToUser_shouldReturnUser() {
        // Arrange
        User expected = user;

        // Act
        User actual = (User) AccountConverter.convertToAccount(userEntity);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getProfilePicture(), actual.getProfilePicture());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getBio(), actual.getBio());
    }

    @Test
    void testConvertAdminEntity_whereProfilePictureIsNull_shouldReturnAdmin() {
        // Arrange
        Admin expected = admin;
        admin.setProfilePicture(null);
        adminEntity.setProfilePicture(null);

        // Act
        Admin actual = (Admin) AccountConverter.convertToAccount(adminEntity);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getProfilePicture(), actual.getProfilePicture());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getCompany(), actual.getCompany());
    }

    @Test
    void testConvertInvalidAccountTypeToAccount_shouldThrowException() {
        // Act and Assert
        assertThrows(UnsupportedAccountTypeException.class, () -> {
            AccountConverter.convertToAccount(accountEntity);
        });
    }

    @Test
    void testConvertInvalidAccountTypeToAccountEntity_shouldThrowException() {
        // Act and Assert
        assertThrows(UnsupportedAccountTypeException.class, () -> {
            AccountConverter.convertToAccountEntity(account);
        });
    }
}
