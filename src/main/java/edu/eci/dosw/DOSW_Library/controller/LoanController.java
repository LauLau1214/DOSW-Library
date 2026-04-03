package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.LoanMapper;
import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.validator.LoanValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Operaciones relacionadas con préstamos")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Crear un préstamo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo creado"),
            @ApiResponse(responseCode = "404", description = "Usuario o libro no encontrado"),
            @ApiResponse(responseCode = "409", description = "Libro no disponible")
    })
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO)
            throws UserNotFoundException, BookNotAvailableException {

        LoanValidator.validate(loanDTO);

        Loan loan = loanService.createLoan(
                LoanMapper.extractBookId(loanDTO),
                LoanMapper.extractUserId(loanDTO)
        );

        return ResponseEntity.status(201).body(LoanMapper.toDTO(loan));
    }

    @Operation(summary = "Devolver un libro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro devuelto"),
            @ApiResponse(responseCode = "400", description = "El libro ya fue devuelto")
    })
    @PutMapping("/return/{loanId}")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable String loanId) {
        Loan loan = loanService.returnBook(loanId);
        return ResponseEntity.ok(LoanMapper.toDTO(loan));
    }

    @Operation(summary = "Obtener todos los préstamos")
    @ApiResponse(responseCode = "200", description = "Lista de préstamos")
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
}