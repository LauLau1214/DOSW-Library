package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    public Loan createLoan(String bookId, String userId)
            throws BookNotAvailableException, UserNotFoundException {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + userId));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("El libro no está disponible: " + bookId);
        }

        bookService.decreaseStock(bookId);

        Loan loan = new Loan();
        loan.setId(UUID.randomUUID().toString());
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(Status.ACTIVE);
        loan.setReturnDate(null);

        return loanRepository.save(loan);
    }

    public Loan returnBook(String loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));

        if (loan.getStatus() == Status.RETURNED) {
            throw new RuntimeException("El libro ya fue devuelto");
        }

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());

        loanRepository.save(loan);

        bookService.increaseStock(loan.getBook().getId());

        return loan;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUserId(String userId) {

        return loanRepository.findAll().stream()
                .filter(loan -> loan.getUser().getId().equals(userId))
                .toList();
    }

    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));
    }

    public void markAsExpired(String loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));

        if (loan.getStatus() == Status.RETURNED) {
            throw new RuntimeException("No se puede expirar un libro ya devuelto");
        }

        loan.setStatus(Status.EXPIRED);
        loanRepository.save(loan);
    }
}