package edu.eci.dosw.DOSW_Library.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private String publicationType;
    private LocalDate publicationDate;
    private List<String> categories;
    private int pages;
    private String language;
    private String publisher;
    private String status;
    private int totalCopies;
    private int availableCopies;
    private int borrowedCopies;
    private LocalDate addedToCatalogDate;

    // campo calculado para compatibilidad
    public boolean isAvailable() {
        return availableCopies > 0;
    }
}