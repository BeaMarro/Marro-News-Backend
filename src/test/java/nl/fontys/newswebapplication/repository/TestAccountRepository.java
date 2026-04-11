package nl.fontys.newswebapplication.repository;

import jakarta.transaction.Transactional;
import nl.fontys.newswebapplication.domain.enums.ApprovalStatus;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestAccountRepository {
    @Mock
    private AccountRepository accountRepository;

    private JournalistEntity journalist;
    private JournalistEntity updatedJournalist;

    private AdminEntity admin;
    private AdminEntity updatedAdmin;

    private UserEntity user;
    private UserEntity updatedUser;

    private AccountEntity account;
    private AccountEntity emptyAccount;
    private AccountEntity updatedAccount;

    private List<AccountEntity> accounts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        int approvalStatusId = ApprovalStatus.APPROVED.ordinal();
        byte[] coverImage = new byte[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] profilePicture = "profile_picture.jpg".getBytes();
        byte[] updatedProfilePicture = "profile_picture_2.jpg".getBytes();

        // Setup Article Entities
        ArticleEntity article1 = ArticleEntity.builder()
                .id(1)
                .heading("Heading 1")
                .text("Text 1")
                .publishDate(LocalDate.now())
                .authorEntity(journalist)
                .genreId(2)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        ArticleEntity article2 = ArticleEntity.builder()
                .id(2)
                .heading("Heading 2")
                .text("Text 2")
                .publishDate(LocalDate.now())
                .authorEntity(journalist)
                .genreId(1)
                .videoLink("www.video.nl")
                .coverImage(coverImage)
                .approvalStatusId(approvalStatusId)
                .build();

        List<ArticleEntity> articles = List.of(article1, article2);

        // Journalist Entity
        journalist = new JournalistEntity();
        journalist.setId(1);
        journalist.setFullName("John Doe");
        journalist.setUsername("J.Doe");
        journalist.setEmail("j.doe@news.com");
        journalist.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalist.setProfilePicture(profilePicture);
        journalist.setPassword("12345678910");
        journalist.setDepartmentId(2);
        journalist.setArticles(articles);

        updatedJournalist = new JournalistEntity();
        updatedJournalist.setId(1);
        updatedJournalist.setFullName("John Doe 2");
        updatedJournalist.setUsername("J.Doe 2");
        updatedJournalist.setEmail("john.doe@news.com");
        updatedJournalist.setDateOfBirth(LocalDate.of(1985, 7, 9));
        updatedJournalist.setProfilePicture(updatedProfilePicture);
        updatedJournalist.setPassword("123ABC123ABC");
        updatedJournalist.setDepartmentId(4);

        // User Entity
        user = new UserEntity();
        user.setId(2);
        user.setFullName("Beatrice Marro");
        user.setUsername("B.Marro");
        user.setEmail("b.marro@student.fontys.nl");
        user.setDateOfBirth(LocalDate.of(2005, 8, 9));
        user.setProfilePicture(profilePicture);
        user.setPassword("Beatrice12345");
        user.setBio("test");
        user.setArticles(articles);
        user.setFavoriteArticles(articles);

        updatedUser = new UserEntity();
        updatedUser.setId(2);
        updatedUser.setFullName("Beatrice Marro 2");
        updatedUser.setUsername("B.Marro 2");
        updatedUser.setEmail("beatrice.marro@student.fontys.nl");
        updatedUser.setDateOfBirth(LocalDate.of(2005, 7, 9));
        updatedUser.setProfilePicture(updatedProfilePicture);
        updatedUser.setPassword("Beatrice12345678910");
        updatedUser.setBio("testing Marro News");

        // Admin Entity
        admin = new AdminEntity();
        admin.setId(3);
        admin.setFullName("Jane Doe");
        admin.setUsername("Jane.Doe");
        admin.setEmail("jane.doe@news.nl");
        admin.setDateOfBirth(LocalDate.of(1995, 6, 10));
        admin.setProfilePicture(profilePicture);
        admin.setPassword("Admin123123");
        admin.setCompany("Fontys");
        admin.setArticles(articles);

        updatedAdmin = new AdminEntity();
        updatedAdmin.setId(3);
        updatedAdmin.setFullName("Janet Doe");
        updatedAdmin.setUsername("Janet.Doe");
        updatedAdmin.setEmail("janet.doe@news.nl");
        updatedAdmin.setDateOfBirth(LocalDate.of(1995, 6, 9));
        updatedAdmin.setProfilePicture(updatedProfilePicture);
        updatedAdmin.setPassword("AdminPassword123ABC");
        updatedAdmin.setCompany("Marro News Ltd");

        // Account Entity
        account = AccountEntity.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake.doe@news.nl")
                .dateOfBirth(LocalDate.of(1990,7,5))
                .profilePicture(profilePicture)
                .password("12345678910")
                .build();

        emptyAccount = null;

        updatedAccount = AccountEntity.builder()
                .id(4)
                .fullName("Jaken Doe")
                .username("Jaken.Doe")
                .email("jaken.doe@news.nl")
                .dateOfBirth(LocalDate.of(1991, 7, 5))
                .profilePicture(updatedProfilePicture)
                .password("Password123456")
                .build();

        // Accounts List
        accounts.add(journalist);
        accounts.add(user);
        accounts.add(admin);
    }

    @Test
    void testGetAllAccounts_shouldReturnAllAccounts() {
        // Arrange
        List<AccountEntity> expected = accounts;
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<AccountEntity> actual = accountRepository.findAll();

        // Assert
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testGetAdminById_shouldReturnAdmin() {
        // Arrange
        int id = admin.getId();
        Optional<AccountEntity> expected = Optional.of(admin);
        when(accountRepository.findById(id)).thenReturn(expected);

        // Act
        Optional<AccountEntity> actual = accountRepository.findById(id);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetJournalistById_shouldReturnJournalist() {
        // Arrange
        int id = admin.getId();
        Optional<AccountEntity> expected = Optional.of(admin);
        when(accountRepository.findById(id)).thenReturn(expected);

        // Act
        Optional<AccountEntity> actual = accountRepository.findById(id);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetUserById_shouldReturnUser() {
        // Arrange
        int id = user.getId();
        Optional<AccountEntity> expected = Optional.of(user);
        when(accountRepository.findById(id)).thenReturn(expected);

        // Act
        Optional<AccountEntity> actual = accountRepository.findById(id);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetAccountByInvalidId_shouldReturnNull() {
        // Arrange
        int invalidId  = 999;
        Optional<AccountEntity> expected = Optional.of(user);
        when(accountRepository.findById(invalidId)).thenReturn(expected);

        // Act
        Optional<AccountEntity> actual = accountRepository.findById(invalidId);

        // Assert
        assertNotNull(actual);
    }

    @Test
    void testExistsByExistingId_shouldReturnTrue() {
        // Arrange
        int id = account.getId();
        when(accountRepository.existsById(id)).thenReturn(true);

        // Act
        boolean exists = accountRepository.existsById(id);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByNonExistingId_shouldReturnFalse() {
        // Arrange
        int invalidId = 999;
        when(accountRepository.existsById(invalidId)).thenReturn(false);

        // Act
        boolean exists = accountRepository.existsById(invalidId);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testExistsByExistingUsername_shouldReturnTrue() {
        // Arrange
        String username = "J.Doe";
        when(accountRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = accountRepository.existsByUsername(username);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByNonExistingUsername_shouldReturnFalse() {
        // Arrange
        String invalidUsername = "User";
        when(accountRepository.existsByUsername(invalidUsername)).thenReturn(false);

        // Act
        boolean exists = accountRepository.existsByUsername(invalidUsername);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testCreateValidAdmin_shouldReturnAdmin() {
        // Arrange
        AccountEntity expected = admin;
        when(accountRepository.save(expected)).thenReturn(expected);

        // Act
        AccountEntity actual = accountRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateValidJournalist_shouldReturnJournalist() {
        // Arrange
        AccountEntity expected = journalist;
        when(accountRepository.save(expected)).thenReturn(expected);

        // Act
        AccountEntity actual = accountRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateValidUser_shouldReturnUser() {
        // Arrange
        AccountEntity expected = user;
        when(accountRepository.save(expected)).thenReturn(expected);

        // Act
        AccountEntity actual = accountRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateEmptyAccount_shouldReturnNull() {
        // Arrange
        when(accountRepository.save(emptyAccount)).thenReturn(null);

        // Act
        AccountEntity actual = accountRepository.save(emptyAccount);

        // Assert
        assertNull(actual);
    }

    @Test
    void testDeleteExistingAccount_shouldReturnNothing() {
        // Arrange
        int accountId = journalist.getId();
        int actual;
        int expected = accounts.size() - 1;
        when(accountRepository.findAll()).thenReturn(List.of(admin, user));

        // Act
        accountRepository.deleteById(accountId);
        actual = accountRepository.findAll().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateValidAdmin_shouldReturnAdmin() {
        // Arrange
        AccountEntity expected = updatedAccount;
        when(accountRepository.save(expected)).thenReturn(expected);

        // Act
        AccountEntity actual = accountRepository.save(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateEmptyAccount_shouldReturnNull() {
        // Arrange
        when(accountRepository.save(emptyAccount)).thenReturn(null);

        // Act
        AccountEntity actual = accountRepository.save(emptyAccount);

        // Assert
        assertNull(actual);
    }
}
