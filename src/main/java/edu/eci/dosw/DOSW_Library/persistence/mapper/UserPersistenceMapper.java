package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;

public class UserPersistenceMapper {

    public static User toModel(UserEntity e) {
        User user = new User();
        user.setId(e.getUserId());
        user.setName(e.getName());
        user.setUsername(e.getUsername());
        user.setPassword(e.getPassword());
        user.setRole(e.getRole());
        user.setEmail(e.getEmail());
        user.setMembershipType(e.getMembershipType());
        user.setRegisteredDate(e.getRegisteredDate());
        return user;
    }

    public static UserEntity toEntity(User user) {
        UserEntity e = new UserEntity();
        e.setUserId(user.getId());
        e.setName(user.getName());
        e.setUsername(user.getUsername());
        e.setPassword(user.getPassword());
        e.setRole(user.getRole());
        e.setEmail(user.getEmail());
        e.setMembershipType(user.getMembershipType());
        e.setRegisteredDate(user.getRegisteredDate());
        return e;
    }
}