package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book, int copies) {
        if (book == null || copies <= 0) {
            throw new RuntimeException("Datos inválidos");
        }

        if (bookRepository.existsById(book.getId())) {
            BookEntity existing = bookRepository.findById(book.getId()).get();
            existing.setTotalCopies(existing.getTotalCopies() + copies);
            existing.setAvailableCopies(existing.getAvailableCopies() + copies);
            bookRepository.save(existing);
        } else {
            BookEntity entity = BookPersistenceMapper.toEntity(book, copies, copies);
            bookRepository.save(entity);
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .map(BookPersistenceMapper::toModel)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
    }

    public void updateBookAvailability(String id, boolean available) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        entity.setAvailable(available);
        if (!available) {
            entity.setAvailableCopies(0);
        } else {
            entity.setAvailableCopies(Math.max(1, entity.getAvailableCopies()));
        }
        bookRepository.save(entity);
    }

    public boolean isAvailable(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        return entity.getAvailableCopies() > 0;
    }

    public void decreaseStock(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        if (entity.getAvailableCopies() <= 0) {
            throw new RuntimeException("No hay stock disponible");
        }
        entity.setAvailableCopies(entity.getAvailableCopies() - 1);
        bookRepository.save(entity);
    }

    public void increaseStock(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));

        if (entity.getAvailableCopies() < entity.getTotalCopies()) {
            entity.setAvailableCopies(entity.getAvailableCopies() + 1);
            bookRepository.save(entity);
        }
    }

    public void removeBook(String id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado: " + id);
        }
        bookRepository.deleteById(id);
    }

    public int getCopies(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        return entity.getAvailableCopies();
    }
}