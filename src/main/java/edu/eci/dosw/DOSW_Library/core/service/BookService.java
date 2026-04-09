package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
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
        if (book == null || copies <= 0) throw new RuntimeException("Datos inválidos");

        if (bookRepository.existsById(book.getId())) {
            Book existing = bookRepository.findById(book.getId()).get();
            existing.setTotalCopies(existing.getTotalCopies() + copies);
            existing.setAvailableCopies(existing.getAvailableCopies() + copies);
            bookRepository.save(existing);
        } else {
            book.setTotalCopies(copies);
            book.setAvailableCopies(copies);
            book.setBorrowedCopies(0);
            book.setStatus(copies > 0 ? "AVAILABLE" : "UNAVAILABLE");
            bookRepository.save(book);
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
    }

    public void updateBookAvailability(String id, boolean available) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        if (!available) {
            book.setStatus("UNAVAILABLE");
            book.setAvailableCopies(0);
        } else {
            book.setStatus("AVAILABLE");
            book.setAvailableCopies(Math.max(1, book.getAvailableCopies()));
        }
        bookRepository.save(book);
    }

    public boolean isAvailable(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id))
                .getAvailableCopies() > 0;
    }

    public void decreaseStock(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        if (book.getAvailableCopies() <= 0) throw new RuntimeException("No hay stock disponible");
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.setBorrowedCopies(book.getBorrowedCopies() + 1);
        if (book.getAvailableCopies() == 0) book.setStatus("UNAVAILABLE");
        bookRepository.save(book);
    }

    public void increaseStock(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id));
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            book.setBorrowedCopies(Math.max(0, book.getBorrowedCopies() - 1));
            book.setStatus("AVAILABLE");
            bookRepository.save(book);
        }
    }

    public void removeBook(String id) {
        if (!bookRepository.existsById(id)) throw new RuntimeException("Libro no encontrado: " + id);
        bookRepository.delete(id);
    }

    public int getCopies(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado: " + id))
                .getAvailableCopies();
    }
}