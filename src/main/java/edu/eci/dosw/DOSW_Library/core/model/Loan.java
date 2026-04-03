package edu.eci.dosw.DOSW_Library.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Loan {

    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private Status status;
    private LocalDate returnDate;

}
