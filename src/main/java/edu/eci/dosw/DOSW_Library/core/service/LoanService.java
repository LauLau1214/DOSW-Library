package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + bookId));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + userId));

        if (bookEntity.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("El libro no está disponible: " + bookId);
        }

        bookService.decreaseStock(bookId);

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setLoanId(UUID.randomUUID().toString());
        loanEntity.setBookId(bookEntity);
        loanEntity.setUser(userEntity);
        loanEntity.setLoanDate(LocalDate.now());
        loanEntity.setStatus(Status.ACTIVE.name());
        loanEntity.setReturnDate(null);

        loanRepository.save(loanEntity);
        return LoanPersistenceMapper.toModel(loanEntity);
    }

    public Loan returnBook(String loanId) {
        LoanEntity loanEntity = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));

        if (loanEntity.getStatus().equals(Status.RETURNED.name())) {
            throw new RuntimeException("El libro ya fue devuelto");
        }

        loanEntity.setStatus(Status.RETURNED.name());
        loanEntity.setReturnDate(LocalDate.now());
        loanRepository.save(loanEntity);

        bookService.increaseStock(loanEntity.getBookId().getBookId());

        return LoanPersistenceMapper.toModel(loanEntity);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(LoanPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId)
                .stream()
                .map(LoanPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId)
                .map(LoanPersistenceMapper::toModel)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));
    }

    public void markAsExpired(String loanId) {
        LoanEntity loanEntity = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado: " + loanId));

        if (loanEntity.getStatus().equals(Status.RETURNED.name())) {
            throw new RuntimeException("No se puede expirar un libro ya devuelto");
        }

        loanEntity.setStatus(Status.EXPIRED.name());
        loanRepository.save(loanEntity);
    }
}