package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    @Test
    void shouldAddBook() {
        Book book = new Book("Libro", "Autor", "1", true);

        bookService.addBook(book, 2);

        assertEquals(2, bookService.getCopies("1"));
    }

    @Test
    void shouldFailIfInvalidBook() {
        assertThrows(RuntimeException.class, () -> {
            bookService.addBook(null, 1);
        });
    }

    @Test
    void shouldDecreaseStock() {
        Book book = new Book("Libro", "Autor", "1", true);

        bookService.addBook(book, 2);
        bookService.decreaseStock("1");

        assertEquals(1, bookService.getCopies("1"));
    }

    @Test
    void shouldFailIfNoStock() {
        Book book = new Book("Libro", "Autor", "1", true);

        bookService.addBook(book, 1);
        bookService.decreaseStock("1");

        assertThrows(RuntimeException.class, () -> {
            bookService.decreaseStock("1");
        });
    }

    @Test
    void shouldFailWhenBookIsNull() {
        assertThrows(RuntimeException.class, () -> {
            bookService.addBook(null, 5);
        });
    }

    @Test
    void shouldFailWhenCopiesZero() {
        Book book = new Book("A", "B", "1", true);

        assertThrows(RuntimeException.class, () -> {
            bookService.addBook(book, 0);
        });
    }

    @Test
    void shouldFailWhenBookNotFound() {
        assertThrows(RuntimeException.class, () -> {
            bookService.getBookById("999");
        });
    }

    @Test
    void shouldFailWhenNoStock() {
        Book book = new Book("A", "B", "1", true);
        bookService.addBook(book, 1);

        bookService.decreaseStock("1");

        assertThrows(RuntimeException.class, () -> {
            bookService.decreaseStock("1");
        });
    }
}
