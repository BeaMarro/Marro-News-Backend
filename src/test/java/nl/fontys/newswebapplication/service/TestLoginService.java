package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.config.security.token.AccessTokenEncoder;
import nl.fontys.newswebapplication.config.security.token.impl.AccessTokenImpl;
import nl.fontys.newswebapplication.domain.request.LoginRequest;
import nl.fontys.newswebapplication.domain.response.LoginResponse;
import nl.fontys.newswebapplication.repositories.AccountRepository;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.LoginService;
import nl.fontys.newswebapplication.services.exceptions.account.InvalidAccountTypeException;
import nl.fontys.newswebapplication.services.exceptions.login.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestLoginService {
    @Mock
    private AccountRepository accountRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @InjectMocks
    private LoginService loginService;

    private AdminEntity adminEntity;
    private JournalistEntity journalistEntity;
    private UserEntity userEntity;
    private AccountEntity accountEntity;

    private LoginRequest adminLoginRequest;
    private LoginRequest journalistLoginRequest;
    private LoginRequest userLoginRequest;

    @BeforeEach
    void setup() {
        byte[] profilePicture = "profile_picture.jpg".getBytes();

        // Login Request
        adminLoginRequest = LoginRequest.builder()
                .username("Admin")
                .password("admin1234")
                .build();

        journalistLoginRequest = LoginRequest.builder()
                .username("J.Doe")
                .password("j123j123j123")
                .build();

        userLoginRequest = LoginRequest.builder()
                .username("B.Marro")
                .password("12345678910")
                .build();

        // Journalist Entity
        journalistEntity = new JournalistEntity();
        journalistEntity.setId(1);
        journalistEntity.setFullName("John Doe");
        journalistEntity.setUsername("J.Doe");
        journalistEntity.setEmail("j.doe@news.com");
        journalistEntity.setDateOfBirth(LocalDate.of(1985, 6, 9));
        journalistEntity.setProfilePicture(profilePicture);
        journalistEntity.setPassword("j123j123j123");
        journalistEntity.setDepartmentId(2);

        // User Entity
        userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setFullName("Beatrice Marro");
        userEntity.setUsername("B.Marro");
        userEntity.setEmail("b.marro@student.fontys.nl");
        userEntity.setDateOfBirth(LocalDate.of(2005, 7, 9));
        userEntity.setProfilePicture(profilePicture);
        userEntity.setPassword("12345678910");
        userEntity.setBio("test");

        // Admin Entity
        adminEntity = new AdminEntity();
        adminEntity.setId(3);
        adminEntity.setFullName("Jane Doe");
        adminEntity.setUsername("Admin");
        adminEntity.setEmail("jane.doe@news.nl");
        adminEntity.setDateOfBirth(LocalDate.of(1995, 6, 10));
        adminEntity.setProfilePicture(profilePicture);
        adminEntity.setPassword("admin1234");
        adminEntity.setCompany("Fontys");

        // Account Entity
        accountEntity = AccountEntity.builder()
                .id(4)
                .fullName("Jake Doe")
                .username("Jake.Doe")
                .email("jake@doe.nl")
                .dateOfBirth(LocalDate.of(1997, 8, 6))
                .profilePicture(profilePicture)
                .password("ABC1234ABC1234")
                .build();
    }

    @Test
    void testLoginUserUsingCorrectCredentials_shouldReturnAccessToken() {
        // Arrange
        LoginRequest loginRequest = userLoginRequest;
        int id = userEntity.getId();
        String role = "USER";
        String username = loginRequest.getUsername();
        String mockAccessToken = "Newly Generated Access Token";
        AccessTokenImpl accessToken = new AccessTokenImpl(username, id, role);

        when(accountRepositoryMock.getAccountEntityByUsername(username)).thenReturn(userEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(accessToken)).thenReturn(mockAccessToken);

        // Act
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Assert
        assertNotNull(loginResponse.getAccessToken());
        assertEquals(mockAccessToken, loginResponse.getAccessToken());

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
        verify(accessTokenEncoder).encode(accessToken);
    }

    @Test
    void testLoginAdminUsingCorrectCredentials_shouldReturnAccessToken() {
        // Arrange
        LoginRequest loginRequest = adminLoginRequest;
        int id = adminEntity.getId();
        String role = "ADMIN";
        String username = loginRequest.getUsername();
        String mockAccessToken = "Newly Generated Access Token";
        AccessTokenImpl accessToken = new AccessTokenImpl(username, id, role);

        when(accountRepositoryMock.getAccountEntityByUsername(username)).thenReturn(adminEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), adminEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(accessToken)).thenReturn(mockAccessToken);

        // Act
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Assert
        assertNotNull(loginResponse.getAccessToken());
        assertEquals(mockAccessToken, loginResponse.getAccessToken());

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
        verify(accessTokenEncoder).encode(accessToken);
    }

    @Test
    void testLoginJournalistUsingCorrectCredentials_shouldReturnAccessToken() {
        // Arrange
        LoginRequest loginRequest = journalistLoginRequest;
        int id = journalistEntity.getId();
        String role = "JOURNALIST";
        String username = loginRequest.getUsername();
        String mockAccessToken = "Newly Generated Access Token";
        AccessTokenImpl accessToken = new AccessTokenImpl(username, id, role);

        when(accountRepositoryMock.getAccountEntityByUsername(username)).thenReturn(journalistEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), journalistEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(accessToken)).thenReturn(mockAccessToken);

        // Act
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Assert
        assertNotNull(loginResponse.getAccessToken());
        assertEquals(mockAccessToken, loginResponse.getAccessToken());

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
        verify(accessTokenEncoder).encode(accessToken);
    }

    @Test
    void testLoginUsingIncorrectPassword_shouldThrowException() {
        // Arrange
        String username  = userEntity.getUsername();
        LoginRequest loginRequest = LoginRequest.builder()
                .username(userEntity.getUsername())
                .password("password1234")
                .build();

        when(accountRepositoryMock.getAccountEntityByUsername(loginRequest.getUsername())).thenReturn(userEntity);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
    }

    @Test
    void testLoginUsingIncorrectUsername_shouldThrowException() {
        // Arrange
        String username = "invalidUsername";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password("12345678910")
                .build();

        when(accountRepositoryMock.getAccountEntityByUsername(loginRequest.getUsername())).thenReturn(null);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
    }

    @Test
    void testLoginUsingInvalidAccountType_shouldThrowException() {
        // Arrange
        String username = accountEntity.getUsername();
        String password = accountEntity.getPassword();
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        when(accountRepositoryMock.getAccountEntityByUsername(loginRequest.getUsername())).thenReturn(accountEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), loginRequest.getPassword())).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidAccountTypeException.class, () -> {
            loginService.login(loginRequest);
        });

        verify(accountRepositoryMock).getAccountEntityByUsername(username);
    }
}
