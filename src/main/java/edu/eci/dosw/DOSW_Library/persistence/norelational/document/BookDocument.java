package edu.eci.dosw.DOSW_Library.persistence.norelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDocument {

    @Id
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
}