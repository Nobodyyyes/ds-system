package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.dtos.PublicKeyDto;
import esmukanov.ds.system.entities.UserKeyEntity;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.KeyMapper;
import esmukanov.ds.system.models.UserKey;
import esmukanov.ds.system.repositories.KeyRepository;
import esmukanov.ds.system.services.KeyService;
import esmukanov.ds.system.services.UserService;
import esmukanov.ds.system.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class KeyServiceImpl extends BaseCrudOperationImpl<UserKey, UserKeyEntity, UUID> implements KeyService {

    private final KeyRepository keyRepository;

    private final KeyMapper keyMapper;

    private final UserService userService;

    @Value("${app.master-key}")
    private String masterKeyEnv;


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
     * Генерирует пару ключей RSA для пользователя и сохраняет их в БД.
     *
     * @param userId идентификатор пользователя
     */
    @Override
    @Transactional
    public void generateKeyPair(UUID userId) throws IllegalAccessException {
        if (!userService.isExistsUserByUuid(userId)) {
            throw new NotFoundException("User by ID [%s] not found".formatted(userId));
        }

        PublicKeyDto generatedKeyPair = processGenerateKeyPair(userId);
        log.info("Generated new key pair for user [{}], version [{}]", userId, generatedKeyPair.version());
        log.info("Public key [{}] for user [{}]", generatedKeyPair.publicKey(), userId);
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
        CryptoUtils.EncryptedData data = new CryptoUtils.EncryptedData(userKey.getPrivateKeyIv(), userKey.getPrivateKeyEncrypted());
        SecretKey masterKey = CryptoUtils.getMasterKeyFromEnv(masterKeyEnv);
        byte[] privateBytes = CryptoUtils.decryptAESGCM(data, masterKey);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
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
        UserKey userKey = findKeysByUserId(userId)
                .orElseThrow(() -> new NotFoundException("UserKey by ID [%s] not found".formatted(userId)));

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
     * Если пользователь не существует, метод завершается без действий.
     *
     * @param userId идентификатор пользователя
     */
    @Override
    public void deleteKeys(UUID userId) {
        if (userService.isExistsUserByUuid(userId)) {
            keyRepository.deleteAllByUserId(userId);
        }
    }

    /**
     * Выполняет ротацию ключа пользователя: деактивирует текущие ключи и генерирует новую пару ключей.
     *
     * @param userId идентификатор пользователя
     * @return PublicKeyDto объект с новым публичным ключом и его версией
     * @throws NotFoundException если пользователь с указанным идентификатором уже существует
     * @throws RuntimeException  при ошибке ротации ключа
     */
    @Override
    public PublicKeyDto rotateKey(UUID userId) {
        try {
            if (!userService.isExistsUserByUuid(userId)) {
                throw new NotFoundException("User by ID [%s] not found".formatted(userId));
            }

            deactivateKey(userId);

            return processGenerateKeyPair(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate key for user " + userId, e);
        }
    }

    private void deactivateKey(UUID userId) {
        List<UserKey> userKeys = keyMapper.toModels(keyRepository.findAllByUserId(userId));
        userKeys.forEach(key -> {
            if (!key.isRevoked()) {
                key.setRevoked(true);
                key.setRevokedAt(LocalDateTime.now());
            }
        });

        keyRepository.saveAll(keyMapper.toEntities(userKeys));
    }

    private PublicKeyDto processGenerateKeyPair(UUID userId) throws IllegalAccessException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            SecretKey masterKey = CryptoUtils.getMasterKeyFromEnv(masterKeyEnv);

            byte[] privateBytes = keyPair.getPrivate().getEncoded();
            CryptoUtils.EncryptedData encrypted = CryptoUtils.encryptAESGCM(privateBytes, masterKey);

            int currentVersion = keyRepository.findCurrentVersion(userId);
            int newVersion = currentVersion + 1;

            UserKey userKey = UserKey.builder()
                    .userId(userId)
                    .privateKeyEncrypted(encrypted.cipherText())
                    .privateKeyIv(encrypted.iv())
                    .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
                    .createdAt(LocalDateTime.now())
                    .isRevoked(false)
                    .version(newVersion)
                    .build();

            keyRepository.save(keyMapper.toEntity(userKey));

            return new PublicKeyDto(userKey.getPublicKey(), userKey.getVersion());
        } catch (Exception e) {
            throw new IllegalAccessException("Failed generate key pair");
        }
    }
}
