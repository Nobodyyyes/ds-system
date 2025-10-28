package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.entities.UserKeyEntity;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.KeyMapper;
import esmukanov.ds.system.models.UserKey;
import esmukanov.ds.system.repositories.KeyRepository;
import esmukanov.ds.system.services.KeyService;
import esmukanov.ds.system.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class KeyServiceImpl extends BaseCrudOperationImpl<UserKey, UserKeyEntity, UUID> implements KeyService {

    private final KeyRepository keyRepository;

    private final KeyMapper keyMapper;

    private final UserService userService;

    public KeyServiceImpl(KeyRepository keyRepository, KeyMapper keyMapper, UserService userService) {
        super(keyRepository, keyMapper);
        this.keyRepository = keyRepository;
        this.keyMapper = keyMapper;
        this.userService = userService;
    }

    /**
     * Находит ключ пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return {@link UserKey} объект ключа пользователя, если найден
     */
    @Override
    public Optional<UserKey> findKeysByUserId(UUID userId) {
        return keyRepository.findByUserId(userId).map(keyMapper::toModel);
    }

    /**
     * Генерирует пару ключей RSA для пользователя и сохраняет их в репозитории.
     *
     * @param userId идентификатор пользователя
     * @throws NoSuchAlgorithmException если алгоритм RSA не поддерживается
     */
    @Override
    @Transactional
    public void generateKeyPair(UUID userId) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        // Проверяем, существует ли такой юзер вообще
        if (!userService.existsUser(userId)) throw new NotFoundException("User by ID [%s] not found");

        UserKey userKey = UserKey.builder()
                .userId(userId)
                .privateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
                .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
                .build();

        keyRepository.save(keyMapper.toEntity(userKey));
    }

    /**
     * Получает приватный ключ пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return PrivateKey приватный ключ пользователя
     * @throws NoSuchAlgorithmException если алгоритм RSA не поддерживается
     * @throws InvalidKeySpecException  если спецификация ключа некорректна
     * @throws NotFoundException        если ключ пользователя не найден
     */
    @Override
    public PrivateKey getPrivateKey(UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserKey userKey = findKeysByUserId(userId).orElseThrow(() -> new NotFoundException("UserKey by ID [%s] not found".formatted(userId)));
        byte[] keyBytes = Base64.getDecoder().decode(userKey.getPrivateKey());
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    /**
     * Возвращает публичный RSA ключ пользователя по его идентификатору.
     * Выполняет поиск сохранённой пары ключей, декодирует Base64 представление
     * и восстанавливает объект PublicKey через X509EncodedKeySpec.
     *
     * @param userId идентификатор пользователя
     * @return публичный ключ пользователя
     * @throws NotFoundException если ключ пользователя не найден
     * @throws RuntimeException  при ошибке восстановления ключа (алгоритм или спецификация)
     */
    @Override
    public PublicKey getPublicKey(UUID userId) {
        UserKey userKey = findKeysByUserId(userId).orElseThrow(() -> new NotFoundException("UserKey by ID [%s] not found".formatted(userId)));

        try {
            byte[] keyBytes = Base64.getDecoder().decode(userKey.getPublicKey());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to generate public key for user " + userId, e);
        }
    }

    /**
     * Получает публичный ключ как строку пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return PublicKey публичный ключ пользователя
     * @throws NotFoundException если ключ пользователя не найден
     */
    @Override
    public String getPublicKeyAsString(UUID userId) {
        UserKey userKey = findKeysByUserId(userId).orElseThrow(() -> new NotFoundException("UserKey by ID [%s] not found".formatted(userId)));
        return userKey.getPublicKey();
    }

    /**
     * Удаляет все ключи пользователя по его идентификатору.
     *
     * Если пользователь не существует, метод завершается без действий.
     *
     * @param userId идентификатор пользователя
     */
    @Override
    public void deleteKeys(UUID userId) {
        if (userService.existsUser(userId)) keyRepository.deleteAllByUserId(userId);
    }
}
