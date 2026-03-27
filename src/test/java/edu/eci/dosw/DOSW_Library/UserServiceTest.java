package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
    }

    @Test
    void shouldRegisterUser() {
        User user = new User("1", "Juan");

        userService.registerUser(user);

        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldFailIfUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById("999");
        });
    }

    @Test
    void shouldFailWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById("999");
        });
    }
}
