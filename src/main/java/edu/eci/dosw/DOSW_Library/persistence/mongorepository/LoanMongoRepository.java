package edu.eci.dosw.DOSW_Library.persistence.mongorepository;

import edu.eci.dosw.DOSW_Library.persistence.mongodocument.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {

    List<LoanDocument> findByUserId(String userId);
    List<LoanDocument> findByBookId(String bookId);
    List<LoanDocument> findByStatus(String status);
}