package edu.eci.dosw.DOSW_Library.core.service;


import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<User>();

    public void registerUser(User user) {
        if (user == null){
            throw new RuntimeException("Usuario invalido");
        }
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void updateUser(String id, User updatedUser) throws UserNotFoundException {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
    }

    public void deleteUser(String id) throws UserNotFoundException {
        User user = getUserById(id);
        users.remove(user);
    }
}
