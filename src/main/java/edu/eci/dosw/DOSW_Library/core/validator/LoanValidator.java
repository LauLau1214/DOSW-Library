package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;

public class LoanValidator {

    public static void validate(LoanDTO loanDTO) {
        if (loanDTO.getBookId() == null || loanDTO.getUserId() == null) {
            throw new RuntimeException("Datos incompletos");
        }
    }
}
