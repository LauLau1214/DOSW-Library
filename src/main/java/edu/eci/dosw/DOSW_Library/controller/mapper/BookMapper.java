package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.core.model.Book;

public class BookMapper {

    public static Book toModel(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationType(bookDTO.getPublicationType());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setCategories(bookDTO.getCategories());
        book.setPages(bookDTO.getPages());
        book.setLanguage(bookDTO.getLanguage());
        book.setPublisher(bookDTO.getPublisher());
        book.setStatus("AVAILABLE");
        return book;
    }
}