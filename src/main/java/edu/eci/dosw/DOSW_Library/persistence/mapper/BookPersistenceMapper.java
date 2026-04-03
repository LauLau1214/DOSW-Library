package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;

public class BookPersistenceMapper {

    public static Book toModel(BookEntity e) {
        Book book = new Book();
        book.setId(e.getBookId());
        book.setTitle(e.getTitle());
        book.setAuthor(e.getAuthor());
        book.setIsbn(e.getIsbn());
        book.setPublicationType(e.getPublicationType());
        book.setPublicationDate(e.getPublicationDate());
        book.setCategories(e.getCategories());
        book.setPages(e.getPages());
        book.setLanguage(e.getLanguage());
        book.setPublisher(e.getPublisher());
        book.setStatus(e.getStatus());
        book.setTotalCopies(e.getTotalCopies());
        book.setAvailableCopies(e.getAvailableCopies());
        book.setBorrowedCopies(e.getBorrowedCopies());
        book.setAddedToCatalogDate(e.getAddedToCatalogDate());
        return book;
    }

    public static BookEntity toEntity(Book book) {
        BookEntity e = new BookEntity();
        e.setBookId(book.getId());
        e.setTitle(book.getTitle());
        e.setAuthor(book.getAuthor());
        e.setIsbn(book.getIsbn());
        e.setPublicationType(book.getPublicationType());
        e.setPublicationDate(book.getPublicationDate());
        e.setCategories(book.getCategories());
        e.setPages(book.getPages());
        e.setLanguage(book.getLanguage());
        e.setPublisher(book.getPublisher());
        e.setStatus(book.getStatus());
        e.setTotalCopies(book.getTotalCopies());
        e.setAvailableCopies(book.getAvailableCopies());
        e.setBorrowedCopies(book.getBorrowedCopies());
        e.setAddedToCatalogDate(book.getAddedToCatalogDate());
        return e;
    }
}