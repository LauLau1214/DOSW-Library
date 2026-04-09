package edu.eci.dosw.DOSW_Library.persistence.relational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.UserEntity;

public class LoanPersistenceMapper {

    public static Loan toModel(LoanEntity loanEntity) {
        Loan loan = new Loan();
        loan.setId(loanEntity.getLoanId());
        loan.setBook(BookPersistenceMapper.toModel(loanEntity.getBook()));
        loan.setUser(UserPersistenceMapper.toModel(loanEntity.getUserId()));
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

        if (loan.getBook() != null) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setBookId(loan.getBook().getId());
            loanEntity.setBook(bookEntity);
        }

        if (loan.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(loan.getUser().getId());
            loanEntity.setUserId(userEntity);
        }

        return loanEntity;
    }
}