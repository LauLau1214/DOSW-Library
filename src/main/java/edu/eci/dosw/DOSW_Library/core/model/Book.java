package edu.eci.dosw.DOSW_Library.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Book {

    private String title;
    private String author;
    private String id;
    private boolean available;

}
