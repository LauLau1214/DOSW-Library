package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;

public class UserPersistenceMapper {

    public static User toModel(UserEntity entity) {
        return new User(
                entity.getUserId(),
                entity.getName(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
