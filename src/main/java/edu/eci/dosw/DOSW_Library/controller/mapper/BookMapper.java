package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.core.model.Book;

public class BookMapper {

    public static Book toModel(BookDTO bookDTO) {
        return new Book(bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getId(),
                true);
    }
}
