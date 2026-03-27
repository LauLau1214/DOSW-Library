package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;

public class LoanPersistenceMapper {

    public static Loan toModel(LoanEntity loanEntity) {
        Loan loan = new Loan();
        loan.setId(loanEntity.getLoanId());
        loan.setBook(BookPersistenceMapper.toModel(loanEntity.getBookId()));
        loan.setUser(UserPersistenceMapper.toModel(loanEntity.getUser()));
        loan.setLoanDate(loanEntity.getLoanDate());
        loan.setStatus(Status.valueOf(loanEntity.getStatus()));
        loan.setReturnDate(loanEntity.getReturnDate());
        return loan;
    }

    public static LoanEntity toEntity(Loan loan) {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setLoanId(loan.getId());
        loanEntity.setLoanDate(loan.getLoanDate());
        loanEntity.setStatus(loan.getStatus().name());
        loanEntity.setReturnDate(loan.getReturnDate());
        return loanEntity;
    }
}
