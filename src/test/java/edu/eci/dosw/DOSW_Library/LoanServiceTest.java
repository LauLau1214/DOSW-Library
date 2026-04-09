package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;
    private Loan loan;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId("book-001");
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setAvailableCopies(3);
        book.setTotalCopies(3);
        book.setStatus("AVAILABLE");

        user = new User();
        user.setId("user-001");
        user.setName("Laura");
        user.setUsername("lvalentina");
        user.setPassword("1234");
        user.setRole("USER");

        loan = new Loan();
        loan.setId("loan-001");
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(Status.ACTIVE);
    }

    @Test
    void createLoan_exitoso() throws BookNotAvailableException, UserNotFoundException {
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-001")).thenReturn(Optional.of(user));
        when(loanRepository.save(any())).thenReturn(loan);

        Loan result = loanService.createLoan("book-001", "user-001");

        assertNotNull(result);
        verify(bookService, times(1)).decreaseStock("book-001");
        verify(loanRepository, times(1)).save(any());
    }

    @Test
    void createLoan_libroNoDisponible_lanzaExcepcion() {
        book.setAvailableCopies(0);
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-001")).thenReturn(Optional.of(user));

        assertThrows(BookNotAvailableException.class, () ->
                loanService.createLoan("book-001", "user-001")
        );
        verify(bookService, never()).decreaseStock(any());
    }

    @Test
    void createLoan_libroNoExiste_lanzaExcepcion() {
        when(bookRepository.findById("book-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                loanService.createLoan("book-999", "user-001")
        );
    }

    @Test
    void createLoan_usuarioNoExiste_lanzaExcepcion() {
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-999")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                loanService.createLoan("book-001", "user-999")
        );
    }

    @Test
    void returnBook_exitoso() {
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenReturn(loan);

        Loan result = loanService.returnBook("loan-001");

        assertNotNull(result);
        assertEquals(Status.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
        verify(bookService, times(1)).increaseStock("book-001");
    }

    @Test
    void returnBook_yaDevuelto_lanzaExcepcion() {
        loan.setStatus(Status.RETURNED);
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loan));

        assertThrows(RuntimeException.class, () ->
                loanService.returnBook("loan-001")
        );
        verify(bookService, never()).increaseStock(any());
    }

    @Test
    void returnBook_prestamoNoExiste_lanzaExcepcion() {
        when(loanRepository.findById("loan-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                loanService.returnBook("loan-999")
        );
    }

    @Test
    void markAsExpired_exitoso() {
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenReturn(loan);

        loanService.markAsExpired("loan-001");

        assertEquals(Status.EXPIRED, loan.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void markAsExpired_libroYaDevuelto_lanzaExcepcion() {
        loan.setStatus(Status.RETURNED);
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loan));

        assertThrows(RuntimeException.class, () ->
                loanService.markAsExpired("loan-001")
        );
    }

    @Test
    void getAllLoans_retornaLista() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<Loan> result = loanService.getAllLoans();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getLoansByUserId_retornaLista() {
        when(loanRepository.findByUserId("user-001")).thenReturn(List.of(loan));

        List<Loan> result = loanService.getLoansByUser("user-001");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}