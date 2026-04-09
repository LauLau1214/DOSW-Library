package edu.eci.dosw.DOSW_Library.persistence.norelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.norelational.document.UserDocument;

public class UserDocumentMapper {

    public static UserDocument toDocument(User user) {
        UserDocument doc = new UserDocument();
        doc.setId(user.getId());
        doc.setName(user.getName());
        doc.setUsername(user.getUsername());
        doc.setPassword(user.getPassword());
        doc.setRole(user.getRole());
        doc.setEmail(user.getEmail());
        doc.setMembershipType(user.getMembershipType());
        doc.setRegisteredDate(user.getRegisteredDate());
        return doc;
    }

    public static User toDomain(UserDocument doc) {
        User user = new User();
        user.setId(doc.getId());
        user.setName(doc.getName());
        user.setUsername(doc.getUsername());
        user.setPassword(doc.getPassword());
        user.setRole(doc.getRole());
        user.setEmail(doc.getEmail());
        user.setMembershipType(doc.getMembershipType());
        user.setRegisteredDate(doc.getRegisteredDate());
        return user;
    }
}