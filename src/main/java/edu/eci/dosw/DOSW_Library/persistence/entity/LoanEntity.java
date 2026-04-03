package edu.eci.dosw.DOSW_Library.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "loans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String loanId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private String status;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "loan_id")
    private List<LoanHistoryEntity> history;
}