package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.controller.dto.UserDTO;

public class UserValidator {

    public static void validate(UserDTO userDTO){
        if (userDTO.getId() == null || userDTO.getId().isEmpty()){
            throw new RuntimeException("ID requerido");
        }
    }
}
