package edu.eci.dosw.DOSW_Library;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.core.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(bookService, userService);
    }

    @Test
    void shouldCreateLoan() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 2);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("1", "1");

        assertEquals("1", loan.getBook().getId());
        assertEquals("1", loan.getUser().getId());
    }

    @Test
    void shouldFailIfNoStock() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        loanService.createLoan("1", "1"); // deja stock en 0

        assertThrows(Exception.class, () -> {
            loanService.createLoan("1", "1");
        });
    }

    @Test
    void shouldFailIfUserNotFound() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        bookService.addBook(book, 1);

        assertThrows(UserNotFoundException.class, () -> {
            loanService.createLoan("1", "999");
        });
    }

    @Test
    void shouldReturnBook() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("1", "1");

        Loan returned = loanService.returnBook(loan.getId());

        assertEquals(Status.RETURNED, returned.getStatus());
    }

    @Test
    void shouldFailIfReturnTwice() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("1", "1");
        loanService.returnBook(loan.getId());

        assertThrows(RuntimeException.class, () -> {
            loanService.returnBook(loan.getId());
        });
    }

    @Test
    void shouldGetLoansByUser() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 2);
        userService.registerUser(user);

        loanService.createLoan("1", "1");

        assertEquals(1, loanService.getLoansByUserId("1").size());
    }

    @Test
    void shouldMarkAsExpired() throws Exception {
        Book book = new Book("Libro", "Autor", "1", true);
        User user = new User("1", "Juan");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("1", "1");

        loanService.markAsExpired(loan.getId());

        assertEquals(Status.EXPIRED, loan.getStatus());
    }

    @Test
    void shouldFailIfBookNotAvailable() {
        Book book = new Book("A", "B", "1", true);
        User user = new User("1", "Valen");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        bookService.decreaseStock("1");

        assertThrows(Exception.class, () -> {
            loanService.createLoan("1", "1");
        });
    }



}

