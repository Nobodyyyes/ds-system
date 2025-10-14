package esmukanov.ds.system.mappers;

import esmukanov.ds.system.components.BaseMapper;
import esmukanov.ds.system.users.entities.UserEntity;
import esmukanov.ds.system.users.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements BaseMapper<User, UserEntity> {

    @Override
    public User toModel(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .userStatus(entity.getUserStatus())
                .role(entity.getRole())
                .registeredDate(entity.getRegisteredDate())
                .lastLoginDate(entity.getLastLoginDate())
                .build();
    }

    @Override
    public UserEntity toEntity(User model) {
        if (model == null) return null;

        return UserEntity.builder()
                .id(model.getId())
                .username(model.getUsername())
                .email(model.getEmail())
                .password(model.getPassword())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .middleName(model.getMiddleName())
                .userStatus(model.getUserStatus())
                .role(model.getRole())
                .registeredDate(model.getRegisteredDate())
                .lastLoginDate(model.getLastLoginDate())
                .build();
    }
}
