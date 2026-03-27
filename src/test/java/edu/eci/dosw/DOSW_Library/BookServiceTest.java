package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    void shouldAddBook() {
        Book book = new Book("Libro", "Autor", "1", true);

        Mockito.when(bookRepository.existsById("1")).thenReturn(false);

        Mockito.when(bookRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        BookEntity entity = new BookEntity();
        entity.setBookId("1");
        entity.setAvailableCopies(2);
        entity.setTotalCopies(2);

        Mockito.when(bookRepository.findById("1")).thenReturn(java.util.Optional.of(entity));

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
