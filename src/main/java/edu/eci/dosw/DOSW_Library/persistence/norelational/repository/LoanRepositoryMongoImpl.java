package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.norelational.document.LoanDocument;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.BookDocumentMapper;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.LoanDocumentMapper;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.UserDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final LoanMongoRepository loanMongoRepository;
    private final BookMongoRepository bookMongoRepository;
    private final UserMongoRepository userMongoRepository;

    public LoanRepositoryMongoImpl(LoanMongoRepository loanMongoRepository,
                                   BookMongoRepository bookMongoRepository,
                                   UserMongoRepository userMongoRepository) {
        this.loanMongoRepository = loanMongoRepository;
        this.bookMongoRepository = bookMongoRepository;
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Loan save(Loan loan) {
        LoanDocument saved = loanMongoRepository.save(LoanDocumentMapper.toDocument(loan));
        return enrichLoan(saved);
    }

    @Override
    public Optional<Loan> findById(String id) {
        return loanMongoRepository.findById(id).map(this::enrichLoan);
    }

    @Override
    public List<Loan> findAll() {
        return loanMongoRepository.findAll().stream()
                .map(this::enrichLoan)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        loanMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return loanMongoRepository.existsById(id);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return loanMongoRepository.findByUserId(userId).stream()
                .map(this::enrichLoan)
                .collect(Collectors.toList());
    }

    // Reconstruye el Loan completo con Book y User desde MongoDB
    private Loan enrichLoan(LoanDocument doc) {
        Loan loan = LoanDocumentMapper.toDomain(doc);

        if (doc.getBookId() != null) {
            Book book = bookMongoRepository.findById(doc.getBookId())
                    .map(BookDocumentMapper::toDomain)
                    .orElse(null);
            loan.setBook(book);
        }

        if (doc.getUserId() != null) {
            User user = userMongoRepository.findById(doc.getUserId())
                    .map(UserDocumentMapper::toDomain)
                    .orElse(null);
            loan.setUser(user);
        }

        return loan;
    }
}