package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.dtos.request.RegisterRequest;
import esmukanov.ds.system.dtos.response.RegisterResponse;
import esmukanov.ds.system.entities.UserEntity;
import esmukanov.ds.system.enums.Role;
import esmukanov.ds.system.enums.UserStatus;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.UserMapper;
import esmukanov.ds.system.models.User;
import esmukanov.ds.system.repositories.UserRepository;
import esmukanov.ds.system.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseCrudOperationImpl<User, UserEntity, UUID> implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя на основе данных из запроса.
     *
     * @param registerRequest объект запроса с данными пользователя
     * @return ответ с именем пользователя и датой регистрации
     */
    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .middleName(registerRequest.getMiddleName())
                .userStatus(UserStatus.ACTIVE)
                .role(Role.USER)
                .registeredDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .build();

        userRepository.save(userMapper.toEntity(user));

        return new RegisterResponse()
                .setUsername(user.getUsername())
                .setRegisteredDate(LocalDateTime.now());
    }

    /**
     * Выполняет аутентификацию пользователя по имени пользователя и паролю.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return true, если аутентификация успешна, иначе false
     * @throws NotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public boolean loginUser(String username, String password) {
        if (!isExistsUserByUsername(username)) {
            throw new NotFoundException("User by username [%s] not found".formatted(username));
        }

        User user = userMapper.toModel(userRepository.findByUsername(username));
        String userPassword = user.getPassword();
        return passwordEncoder.matches(password, userPassword);
    }

    /**
     * Проверяет существование пользователя по его идентификатору.
     *
     * @param userId уникальный идентификатор пользователя
     * @return true если пользователь существует, иначе false
     */
    @Override
    public boolean isExistsUserByUuid(UUID userId) {
        return userRepository.existsById(userId);
    }

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username имя пользователя для проверки
     * @return true, если пользователь существует, иначе false
     */
    @Override
    public boolean isExistsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
