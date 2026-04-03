package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;

public class BookValidator {

    public static void validate(BookDTO bookDTO){
        if(bookDTO.getId() == null || bookDTO.getId().isEmpty()){
            throw new RuntimeException("ID requerido");
        }
        if (bookDTO.getTitle() == null || bookDTO.getTitle().isEmpty()){
            throw new RuntimeException("Titulo requerido");
        }
    }
}
