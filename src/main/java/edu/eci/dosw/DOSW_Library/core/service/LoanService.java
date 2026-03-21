package edu.eci.dosw.DOSW_Library.core.service;


import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.BookNotFoundException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.core.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private List<Loan> loans = new ArrayList<>();
    private UserService userService;
    private BookService bookService;

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(String bookId, String userId)
            throws BookNotAvailableException, UserNotFoundException {

        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        if (!bookService.isAvailable(bookId)) {
            throw new BookNotAvailableException("El libro no está disponible: " + bookId);
        }

        bookService.decreaseStock(bookId);

        Loan loan = new Loan(
                UUID.randomUUID().toString(),
                book,
                user,
                LocalDate.now(),
                Status.ACTIVE,
                null
        );

        loans.add(loan);
        return loan;
    }


    public Loan returnBook(String loanId) {

        Loan loan = getLoanById(loanId);

        if (loan.getStatus() == Status.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());

        bookService.increaseStock(loan.getBook().getId());

        return loan;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loans.stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Loan> getLoansByBookId(String bookId) {
        return loans.stream()
                .filter(n -> n.getBook().getId().equals(bookId))
                .collect(Collectors.toList());
    }

    public Loan getLoanById(String loanId) {
        return loans.stream()
                .filter(n -> n.getId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado"));
    }

    public void markAsExpired(String loanId) {

        Loan loan = getLoanById(loanId);

        if (loan.getStatus() == Status.RETURNED) {
            throw new RuntimeException("No se puede expirar un libro ya devuelto");
        }

        loan.setStatus(Status.EXPIRED);
    }
}