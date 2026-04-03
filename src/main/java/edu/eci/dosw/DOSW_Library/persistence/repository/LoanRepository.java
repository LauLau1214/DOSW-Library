package edu.eci.dosw.DOSW_Library.persistence.repository;

import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    List<LoanEntity> findByUserUserId(String userId);

    List<LoanEntity> findByBookBookId(String bookId);
}
