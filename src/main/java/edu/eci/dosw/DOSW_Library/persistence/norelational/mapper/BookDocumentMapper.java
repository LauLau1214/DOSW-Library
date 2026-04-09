package edu.eci.dosw.DOSW_Library.persistence.norelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.norelational.document.BookDocument;

public class BookDocumentMapper {

    public static BookDocument toDocument(Book book) {
        BookDocument doc = new BookDocument();
        doc.setId(book.getId());
        doc.setTitle(book.getTitle());
        doc.setAuthor(book.getAuthor());
        doc.setIsbn(book.getIsbn());
        doc.setPublicationType(book.getPublicationType());
        doc.setPublicationDate(book.getPublicationDate());
        doc.setCategories(book.getCategories());
        doc.setPages(book.getPages());
        doc.setLanguage(book.getLanguage());
        doc.setPublisher(book.getPublisher());
        doc.setStatus(book.getStatus());
        doc.setTotalCopies(book.getTotalCopies());
        doc.setAvailableCopies(book.getAvailableCopies());
        doc.setBorrowedCopies(book.getBorrowedCopies());
        doc.setAddedToCatalogDate(book.getAddedToCatalogDate());
        return doc;
    }

    public static Book toDomain(BookDocument doc) {
        Book book = new Book();
        book.setId(doc.getId());
        book.setTitle(doc.getTitle());
        book.setAuthor(doc.getAuthor());
        book.setIsbn(doc.getIsbn());
        book.setPublicationType(doc.getPublicationType());
        book.setPublicationDate(doc.getPublicationDate());
        book.setCategories(doc.getCategories());
        book.setPages(doc.getPages());
        book.setLanguage(doc.getLanguage());
        book.setPublisher(doc.getPublisher());
        book.setStatus(doc.getStatus());
        book.setTotalCopies(doc.getTotalCopies());
        book.setAvailableCopies(doc.getAvailableCopies());
        book.setBorrowedCopies(doc.getBorrowedCopies());
        book.setAddedToCatalogDate(doc.getAddedToCatalogDate());
        return book;
    }
}