package edu.eci.dosw.DOSW_Library.core.service;


import edu.eci.dosw.DOSW_Library.core.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final Map<Book, Integer> books = new HashMap<Book, Integer>();

    public void addBook(Book book, int copies) {
        if(book == null || copies <= 0){
            throw new RuntimeException("Datos invalidos");
        }

        Optional<Book> existingBook = books.keySet()
                .stream()
                .filter(b -> b.getId().equals(book.getId()))
                .findFirst();

        if(existingBook.isPresent()){
            Book b = existingBook.get();
            books.put(b, books.get(b) + copies);
        } else {
            books.put(book, copies);
        }
    }

    public List<Book> getAllBooks() {
        return books.keySet()
                .stream()
                .collect(Collectors.toList());
    }

    public Book getBookById(String id){
        return books.keySet()
                .stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    public void updateBookAvailability(String id, boolean available) {
        Book book = getBookById(id);

        if(!available){
            books.put(book, 0);
        } else {
            books.put(book, Math.max(1, books.get(book)));
        }
    }

    public boolean isAvailable(String id){
        Book book = getBookById(id);
        return books.get(book) > 0;
    }

    public int getCopies(String id){
        Book book = getBookById(id);
        return books.get(book);
    }

    public void updateCopies(String id, int copies) {
        Book book = getBookById(id);
        books.put(book, copies);
    }

    public void removeBook(String id) {
        Book book = getBookById(id);
        books.remove(book);
    }

    public void decreaseStock(String id) {
        Book book = getBookById(id);

        books.computeIfPresent(book, (b, stock) -> {
            if (stock <= 0) {
                throw new RuntimeException("No hay stock disponible");
            }
            return stock - 1;
        });
    }

    public void increaseStock(String id) {
        Book book = getBookById(id);

        books.computeIfPresent(book, (b, stock) -> stock + 1);
    }
}
