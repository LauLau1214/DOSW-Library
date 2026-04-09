package edu.eci.dosw.DOSW_Library.persistence.norelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.Status;
import edu.eci.dosw.DOSW_Library.persistence.norelational.document.LoanDocument;

public class LoanDocumentMapper {

    public static LoanDocument toDocument(Loan loan) {
        LoanDocument doc = new LoanDocument();
        doc.setId(loan.getId());
        doc.setBookId(loan.getBook() != null ? loan.getBook().getId() : null);
        doc.setUserId(loan.getUser() != null ? loan.getUser().getId() : null);
        doc.setLoanDate(loan.getLoanDate());
        doc.setStatus(loan.getStatus() != null ? loan.getStatus().name() : null);
        doc.setReturnDate(loan.getReturnDate());
        return doc;
    }

    public static Loan toDomain(LoanDocument doc) {
        Loan loan = new Loan();
        loan.setId(doc.getId());
        loan.setLoanDate(doc.getLoanDate());
        loan.setStatus(doc.getStatus() != null ? Status.valueOf(doc.getStatus()) : null);
        loan.setReturnDate(doc.getReturnDate());
        // book y user se resuelven en el servicio usando sus repositorios
        return loan;
    }
}