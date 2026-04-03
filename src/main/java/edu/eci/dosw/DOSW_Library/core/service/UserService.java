package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        if (user == null) {
            throw new RuntimeException("Usuario inválido");
        }
        // Encriptar contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity entity = UserPersistenceMapper.toEntity(user);
        userRepository.save(entity);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(UserPersistenceMapper::toModel)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + id));
    }

    public void updateUser(String id, User updatedUser) throws UserNotFoundException {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + id));
        entity.setName(updatedUser.getName());
        userRepository.save(entity);
    }

    public void deleteUser(String id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado: " + id);
        }
        userRepository.deleteById(id);
    }
}