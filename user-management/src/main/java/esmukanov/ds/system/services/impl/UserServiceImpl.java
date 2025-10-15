package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.mappers.UserMapper;
import esmukanov.ds.system.repositories.UserRepository;
import esmukanov.ds.system.services.UserService;
import esmukanov.ds.system.entities.UserEntity;
import esmukanov.ds.system.models.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends BaseCrudOperationImpl<User, UserEntity, UUID> implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
}
