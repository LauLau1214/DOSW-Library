package edu.eci.dosw.DOSW_Library.persistence.mongodocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "loans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDocument {

    @Id
    private String id;
    private String bookId;
    private String userId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String status;
    private List<LoanHistory> history;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanHistory {
        private String status;
        private LocalDate date;
    }
}