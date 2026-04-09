package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.BookDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepository {

    private final BookMongoRepository repository;

    public BookRepositoryMongoImpl(BookMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookDocumentMapper.toDomain(
                repository.save(BookDocumentMapper.toDocument(book))
        );
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id).map(BookDocumentMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream()
                .map(BookDocumentMapper::toDomain)
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