package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.UserDTO;
import edu.eci.dosw.DOSW_Library.core.model.User;

public class UserMapper {

    public static User toModel(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getName());
    }
}
