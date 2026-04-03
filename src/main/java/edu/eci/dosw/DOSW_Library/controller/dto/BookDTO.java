package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private int copies;

    // Campos nuevos
    private String isbn;
    private String publicationType; // Revista, Ebook, Cartilla, etc.
    private LocalDate publicationDate;
    private List<String> categories;
    private int pages;
    private String language;
    private String publisher;
    private LocalDate addedToCatalogDate;
}