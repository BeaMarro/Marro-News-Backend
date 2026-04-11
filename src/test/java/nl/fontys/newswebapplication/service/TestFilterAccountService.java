package nl.fontys.newswebapplication.service;

import nl.fontys.newswebapplication.domain.Article;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.AdminResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.domain.response.UserResponse;
import nl.fontys.newswebapplication.repositories.Filter;
import nl.fontys.newswebapplication.services.FilterAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TestFilterAccountService {
    @Mock
    private Filter filter;
    @InjectMocks
    private FilterAccountService filterAccountService;

    private List<AccountResponse> accounts = new ArrayList<>();
    private JournalistResponse journalist;
    private AdminResponse admin;
    private UserResponse user;

    @BeforeEach
    void setup() {
        // Setup Account objects
        journalist = JournalistResponse.builder()
                .id(2)
                .fullName("John Doe")
                .username("J.Doe")
                .email("j.doe@news.com")
                .dateOfBirth(LocalDate.of(1985, 6, 9))
                .profilePicture("john_doe.jpg")
                .department(Department.COPYRIGHTING)
                .build();

        user = UserResponse.builder()
                .id(2)
                .fullName("Beatrice Marro")
                .username("B.Marro")
                .email("b.marro@student.fontys.nl")
                .dateOfBirth(LocalDate.of(2005, 8, 9))
                .profilePicture("john_doe.jpg")
                .bio("test")
                .build();

        admin = AdminResponse.builder()
                .id(3)
                .fullName("Jane Doe")
                .username("Jane.Doe")
                .email("jane.doe@news.nl")
                .dateOfBirth(LocalDate.of(1995, 6, 10))
                .profilePicture("john_doe.jpg")
                .company("Fontys")
                .build();

        accounts.add(journalist);
        accounts.add(user);
        accounts.add(admin);
    }

    @Test
    void getFilteredAccountsByValidUsername_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String username = user.getUsername();
        List<AccountResponse> filteredAccounts = List.of(user);
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), username)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountsByInvalidUsername_shouldReturnEmptyList() {
        // Arrange
        String username = "Fontys University of Applied Sciences";
        List<AccountResponse> filteredAccounts = List.of();
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), username)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountByValidFullName_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String fullName = user.getFullName();
        List<AccountResponse> filteredAccounts = List.of(user);
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), fullName)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountByValidFullNameSubstring_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String fullName = "Bea";
        List<AccountResponse> filteredAccounts = List.of(user);
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), fullName)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountByInvalidFullName_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String fullName = "Jack Doe";
        List<AccountResponse> filteredAccounts = List.of();
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), fullName)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountByValidEmail_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String email = journalist.getEmail();
        List<AccountResponse> filteredAccounts = List.of(journalist);
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), email)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }

    @Test
    void getFilteredAccountByInvalidEmail_shouldReturnAllFilteredAccountsConverted() {
        // Arrange
        String email = "j.doe@student.fontys.nl";
        List<AccountResponse> filteredAccounts = List.of();
        AccountResponse[] expected = filteredAccounts.toArray(new AccountResponse[0]);

        // Act
        List<AccountResponse> actual = Arrays.stream(filterAccountService.useFilter(accounts.toArray(new AccountResponse[0]), email)).toList();

        // Assert
        assertEquals(expected.length, actual.size());
    }
}
