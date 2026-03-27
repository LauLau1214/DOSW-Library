package edu.eci.dosw.DOSW_Library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTest {

    AuthService authService;

    @Test
    void shouldLoginSuccessfully() {
        String token = authService.login("1", "1234");
        assertNotNull(token);
    }

    @Test
    void shouldFailWithWrongPassword() {
        assertThrows(RuntimeException.class, () -> {
            authService.login("1", "wrong");
        });
    }
}
