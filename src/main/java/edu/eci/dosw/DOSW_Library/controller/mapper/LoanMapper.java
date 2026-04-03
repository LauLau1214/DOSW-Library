package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import edu.eci.dosw.DOSW_Library.core.model.Loan;

public class LoanMapper {

    public static LoanDTO toDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setLoanId(loan.getId());
        dto.setUserId(loan.getUser().getId());
        dto.setBookId(loan.getBook().getId());
        return dto;
    }

    public static String extractBookId(LoanDTO loanDTO) {
        return loanDTO.getBookId();
    }

    public static String extractUserId(LoanDTO loanDTO) {
        return loanDTO.getUserId();
    }
}