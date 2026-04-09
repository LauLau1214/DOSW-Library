package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final LoanMongoRepository repository;

    public LoanRepositoryMongoImpl(LoanMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return LoanDocumentMapper.toDomain(
                repository.save(LoanDocumentMapper.toDocument(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id).map(LoanDocumentMapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream()
                .map(LoanDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}