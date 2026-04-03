package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.Data;

@Data

public class LoanDTO {
    private String loanId;
    private String userId;
    private String bookId;
}
