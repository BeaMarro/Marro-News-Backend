package nl.fontys.newswebapplication.service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestPasswordEncoder {
    @Mock
    PasswordEncoder passwordEncoder;

    private String encodedPassword;

    @BeforeEach
    void setup() {
        encodedPassword = "EncodedPassword";
    }

    @Test
    void testEncodePassword_shouldReturnEncodedPassword() {
        // Arrange
        String password = "admin1234";
        String expected = encodedPassword;
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // Act
        String actual = passwordEncoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertEquals(expected, actual);
    }
}
