package edu.eci.dosw.DOSW_Library.persistence.relational.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loan_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate date;
}