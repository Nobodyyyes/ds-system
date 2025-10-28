package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.mappers.DocumentSignatureMapper;
import esmukanov.ds.system.models.DocumentSignature;
import esmukanov.ds.system.repositories.DocumentSignatureRepository;
import esmukanov.ds.system.services.KeyService;
import esmukanov.ds.system.services.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final DocumentSignatureRepository documentSignatureRepository;

    private final DocumentSignatureMapper documentSignatureMapper;

    private final KeyService keyService;

    /**
     * Подписывает переданный файл для указанного пользователя.
     * Шаги:
     * 1. Считывает байты файла и вычисляет хеш SHA-256.
     * 2. Получает закрытый ключ пользователя через KeyService.
     * 3. Формирует цифровую подпись (алгоритм SHA256withRSA) над хешем.
     * 4. Сохраняет метаданные подписи (без самой подписи) в репозиторий.
     *
     * @param file   исходный файл для подписи
     * @param userId идентификатор пользователя, чей ключ используется
     * @return Base64-представление созданной подписи
     * @throws NoSuchAlgorithmException если алгоритм SHA-256 или SHA256withRSA недоступен
     * @throws InvalidKeySpecException  если спецификация ключа некорректна при извлечении
     * @throws InvalidKeyException      если закрытый ключ некорректен
     * @throws SignatureException       если произошла ошибка при создании подписи
     * @throws IOException              если ошибка чтения содержимого файла
     */
    @Override
    public String signDocument(MultipartFile file, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException {
        byte[] fileBytes = file.getBytes();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);
        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

        PrivateKey privateKey = keyService.getPrivateKey(userId);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hashBytes);
        byte[] signedBytes = signature.sign();
        String signatureBase64 = Base64.getEncoder().encodeToString(signedBytes);

        DocumentSignature documentSignature = DocumentSignature.builder()
                .userId(userId)
                .fileName(file.getOriginalFilename())
                .fileHash(hashBase64)
                .signedDate(LocalDateTime.now())
                .signature(signatureBase64)
                .build();

        documentSignatureRepository.save(documentSignatureMapper.toEntity(documentSignature));

        return signatureBase64;
    }

    /**
     * Проверяет цифровую подпись для заданных данных и пользователя.
     *
     * @param data           данные, для которых проверяется подпись
     * @param signatureBytes байты подписи
     * @param userId         идентификатор пользователя
     * @return true, если подпись действительна, иначе false
     * @throws NoSuchAlgorithmException если алгоритм подписи не найден
     * @throws InvalidKeyException      если ключ некорректен
     * @throws SignatureException       если произошла ошибка при проверке подписи
     */
    @Override
    public boolean verifySignature(byte[] data, byte[] signatureBytes, UUID userId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PublicKey publicKey = keyService.getPublicKey(userId);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data);

        signature.update(hashBytes);

        return signature.verify(signatureBytes);
    }

    @Override
    public byte[] signData(UUID userId, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return new byte[0];
    }
}
