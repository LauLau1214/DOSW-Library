package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.LoanPersistenceMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepository {

    private final LoanRepositoryJpa repository;

    public LoanRepositoryJpaImpl(LoanRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return LoanPersistenceMapper.toModel(
                repository.save(LoanPersistenceMapper.toEntity(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id).map(LoanPersistenceMapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream()
                .map(LoanPersistenceMapper::toModel)
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

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId_UserId(userId).stream()
                .map(LoanPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
}