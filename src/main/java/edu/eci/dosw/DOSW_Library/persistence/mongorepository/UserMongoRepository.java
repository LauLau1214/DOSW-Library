package edu.eci.dosw.DOSW_Library.persistence.mongorepository;

import edu.eci.dosw.DOSW_Library.persistence.mongodocument.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);
    java.util.List<UserDocument> findByMembershipType(String membershipType);
}