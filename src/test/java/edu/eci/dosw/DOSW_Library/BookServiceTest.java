package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId("book-001");
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setAvailableCopies(3);
        book.setTotalCopies(3);
        book.setBorrowedCopies(0);
        book.setStatus("AVAILABLE");
    }

    @Test
    void addBook_nuevo_exitoso() {
        when(bookRepository.existsById("book-001")).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(book);

        bookService.addBook(book, 3);

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void addBook_existente_sumaCopias() {
        when(bookRepository.existsById("book-001")).thenReturn(true);
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        bookService.addBook(book, 2);

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void addBook_libroNulo_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                bookService.addBook(null, 1)
        );
    }

    @Test
    void addBook_copiasEnCero_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
                bookService.addBook(book, 0)
        );
    }

    @Test
    void getBookById_existente_retornaLibro() {
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));

        Book result = bookService.getBookById("book-001");

        assertNotNull(result);
        assertEquals("book-001", result.getId());
    }

    @Test
    void getBookById_noExiste_lanzaExcepcion() {
        when(bookRepository.findById("book-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookService.getBookById("book-999")
        );
    }

    @Test
    void decreaseStock_exitoso() {
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        bookService.decreaseStock("book-001");

        assertEquals(2, book.getAvailableCopies());
        assertEquals(1, book.getBorrowedCopies());
    }

    @Test
    void decreaseStock_sinStock_lanzaExcepcion() {
        book.setAvailableCopies(0);
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));

        assertThrows(RuntimeException.class, () ->
                bookService.decreaseStock("book-001")
        );
    }

    @Test
    void increaseStock_exitoso() {
        book.setAvailableCopies(2);
        book.setBorrowedCopies(1);
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        bookService.increaseStock("book-001");

        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    void getAllBooks_retornaLista() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        var result = bookService.getAllBooks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void removeBook_exitoso() {
        when(bookRepository.existsById("book-001")).thenReturn(true);

        bookService.removeBook("book-001");

        verify(bookRepository, times(1)).delete("book-001");
    }

    @Test
    void removeBook_noExiste_lanzaExcepcion() {
        when(bookRepository.existsById("book-999")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                bookService.removeBook("book-999")
        );
    }
}