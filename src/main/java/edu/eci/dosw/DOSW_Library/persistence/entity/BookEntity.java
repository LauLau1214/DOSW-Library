package edu.eci.dosw.DOSW_Library.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDate;


@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(name = "publication_type", nullable = false)
    private String publicationType; // Revista, Ebook, Cartilla, etc.

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @ElementCollection
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    private List<String> categories;

    // Metadata
    @Column(name = "pages")
    private int pages;

    @Column(name = "language")
    private String language;

    @Column(name = "publisher")
    private String publisher;

    // Availability
    @Column(nullable = false)
    private String status;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(name = "borrowed_copies")
    private int borrowedCopies;

    @Column(name = "added_to_catalog_date")
    private LocalDate addedToCatalogDate;
}