package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;

public class BookPersistenceMapper {

    public static Book toModel(BookEntity bookEntity) {
        Book book = new Book();
        book.setId(bookEntity.getBookId());
        book.setTitle(bookEntity.getTitle());
        book.setAuthor(bookEntity.getAuthor());
        book.setAvailable(bookEntity.getAvailableCopies() > 0);
        return book;
    }

    public static BookEntity toEntity(Book book, int totalCopies, int availableCopies) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookId(book.getId());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setAvailable(availableCopies > 0);
        bookEntity.setTotalCopies(totalCopies);
        bookEntity.setAvailableCopies(availableCopies);
        return bookEntity;
    }
}
