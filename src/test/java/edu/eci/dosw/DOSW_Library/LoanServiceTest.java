package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
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

    private BookEntity bookEntity;
    private UserEntity userEntity;
    private LoanEntity loanEntity;

    @BeforeEach
    void setUp() {
        bookEntity = new BookEntity();
        bookEntity.setBookId("book-001");
        bookEntity.setTitle("Clean Code");
        bookEntity.setAuthor("Robert C. Martin");
        bookEntity.setAvailableCopies(3);
        bookEntity.setTotalCopies(3);
        bookEntity.setStatus("AVAILABLE");

        userEntity = new UserEntity();
        userEntity.setUserId("user-001");
        userEntity.setName("Laura");
        userEntity.setUsername("lvalentina");
        userEntity.setPassword("1234");
        userEntity.setRole("USER");

        loanEntity = new LoanEntity();
        loanEntity.setLoanId("loan-001");
        loanEntity.setBook(bookEntity);
        loanEntity.setUser(userEntity);
        loanEntity.setLoanDate(LocalDate.now());
        loanEntity.setStatus(Status.ACTIVE.name());
    }

    @Test
    void createLoan_exitoso() throws BookNotAvailableException, UserNotFoundException {
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("user-001")).thenReturn(Optional.of(userEntity));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loanEntity);

        Loan result = loanService.createLoan("book-001", "user-001");

        assertNotNull(result);
        verify(bookService, times(1)).decreaseStock("book-001");
        verify(loanRepository, times(1)).save(any(LoanEntity.class));
    }

    @Test
    void createLoan_libroNoDisponible_lanzaExcepcion() {
        bookEntity.setAvailableCopies(0);
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("user-001")).thenReturn(Optional.of(userEntity));

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
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("user-999")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                loanService.createLoan("book-001", "user-999")
        );
    }

    @Test
    void returnBook_exitoso() {
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loanEntity));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loanEntity);

        Loan result = loanService.returnBook("loan-001");

        assertNotNull(result);
        assertEquals(Status.RETURNED.name(), loanEntity.getStatus());
        assertNotNull(loanEntity.getReturnDate());
        verify(bookService, times(1)).increaseStock("book-001");
    }

    @Test
    void returnBook_yaDevuelto_lanzaExcepcion() {
        loanEntity.setStatus(Status.RETURNED.name());
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loanEntity));

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
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loanEntity));

        loanService.markAsExpired("loan-001");

        assertEquals(Status.EXPIRED.name(), loanEntity.getStatus());
        verify(loanRepository, times(1)).save(loanEntity);
    }

    @Test
    void markAsExpired_libroYaDevuelto_lanzaExcepcion() {
        loanEntity.setStatus(Status.RETURNED.name());
        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(loanEntity));

        assertThrows(RuntimeException.class, () ->
                loanService.markAsExpired("loan-001")
        );
    }


    @Test
    void getAllLoans_retornaLista() {
        when(loanRepository.findAll()).thenReturn(List.of(loanEntity));

        List<Loan> result = loanService.getAllLoans();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getLoansByUserId_retornaLista() {
        when(loanRepository.findByUserUserId("user-001")).thenReturn(List.of(loanEntity));

        List<Loan> result = loanService.getLoansByUserId("user-001");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}