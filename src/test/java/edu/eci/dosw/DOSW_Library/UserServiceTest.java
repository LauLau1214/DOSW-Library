package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user-001");
        user.setName("Laura");
        user.setUsername("lvalentina");
        user.setPassword("1234");
        user.setRole("USER");
        user.setEmail("laura@email.com");

        userEntity = new UserEntity();
        userEntity.setUserId("user-001");
        userEntity.setName("Laura");
        userEntity.setUsername("lvalentina");
        userEntity.setPassword("encryptedPassword");
        userEntity.setRole("USER");
        userEntity.setEmail("laura@email.com");
    }

    @Test
    void registerUser_exitoso() {
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userRepository.save(any())).thenReturn(userEntity);

        userService.registerUser(user);

        verify(passwordEncoder, times(1)).encode("1234");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser_usuarioNulo_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser(null)
        );
    }

    @Test
    void getUserById_exitoso() throws UserNotFoundException {
        when(userRepository.findById("user-001")).thenReturn(Optional.of(userEntity));

        User result = userService.getUserById("user-001");

        assertNotNull(result);
        assertEquals("user-001", result.getId());
    }

    @Test
    void getUserById_noExiste_lanzaExcepcion() {
        when(userRepository.findById("user-999")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserById("user-999")
        );
    }

    @Test
    void getAllUsers_retornaLista() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        var result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteUser_exitoso() throws UserNotFoundException {
        when(userRepository.existsById("user-001")).thenReturn(true);

        userService.deleteUser("user-001");

        verify(userRepository, times(1)).deleteById("user-001");
    }

    @Test
    void deleteUser_noExiste_lanzaExcepcion() {
        when(userRepository.existsById("user-999")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser("user-999")
        );
    }
}