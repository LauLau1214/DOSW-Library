package edu.eci.dosw.DOSW_Library.persistence.norelational.document;

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
    private String status;
    private LocalDate returnDate;
    private List<LoanHistoryEntry> history;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoanHistoryEntry {
        private String status;
        private LocalDate executedAt;
    }
}