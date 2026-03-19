package edu.eci.dosw.DOSW_Library.controller;


import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")

public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam String bookId,
                                           @RequestParam String userId) throws UserNotFoundException, BookNotAvailableException {
        return ResponseEntity.status(201).body(loanService.createLoan(bookId, userId));
    }

    @PutMapping("/return/{loanId}")
    public ResponseEntity<Loan> returnBook(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
}
