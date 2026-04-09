package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.BookPersistenceMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepository {

    private final BookRepositoryJpa repository;

    public BookRepositoryJpaImpl(BookRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookPersistenceMapper.toModel(
                repository.save(BookPersistenceMapper.toEntity(book))
        );
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id).map(BookPersistenceMapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream()
                .map(BookPersistenceMapper::toModel)
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