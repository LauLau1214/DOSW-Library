package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.persistence.norelational.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookMongoRepository extends MongoRepository<BookDocument, String> {

    List<BookDocument> findByAuthor(String author);
    List<BookDocument> findByCategoriesContaining(String category);
    List<BookDocument> findByPublicationType(String publicationType);
}