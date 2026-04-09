package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.persistence.norelational.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByUsername(String username);
    Optional<UserDocument> findByEmail(String email);
    java.util.List<UserDocument> findByMembershipType(String membershipType);



}