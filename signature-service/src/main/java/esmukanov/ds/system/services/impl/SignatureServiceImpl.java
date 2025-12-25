package esmukanov.ds.system.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import esmukanov.ds.system.dtos.response.SignedInfo;
import esmukanov.ds.system.mappers.DocumentSignatureMapper;
import esmukanov.ds.system.repositories.DocumentSignatureRepository;
import esmukanov.ds.system.services.KeyService;
import esmukanov.ds.system.services.SignatureService;
import esmukanov.ds.system.utils.ZipUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final DocumentSignatureRepository documentSignatureRepository;

    private final DocumentSignatureMapper documentSignatureMapper;

    private final KeyService keyService;

    private final ObjectMapper objectMapper;

    private static final String SHA_256 = "SHA-256";

    private static final String SHA256_WITH_RSA = "SHA256withRSA";

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
    public byte[] signDocument(MultipartFile file, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException {

        // 1. Получаем байты исходного файла
        byte[] fileBytes = file.getBytes();

        // 2. Хэш документа
        MessageDigest digest = MessageDigest.getInstance(SHA_256);
        byte[] hashBytes = digest.digest(fileBytes);
        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

        // 3. Получаем приватный ключ пользователя
        PrivateKey privateKey = keyService.getPrivateKey(userId);

        // 4. Подписываем хэш документа
        Signature signature = Signature.getInstance(SHA256_WITH_RSA);
        signature.initSign(privateKey);
        signature.update(hashBytes);
        byte[] signedBytes = signature.sign();

        SignedInfo signedInfo = SignedInfo.builder()
                .fileName(file.getOriginalFilename())
                .hashAlgorithm(SHA_256)
                .signatureAlgorithm(SHA256_WITH_RSA)
                .hashFileBase64(hashBase64)
                .signedAt(LocalDateTime.now())
                .userId(userId.toString())
                .publicKeyBase64(Base64.getEncoder().encodeToString(keyService.getPublicKey(userId).getEncoded()))
                .build();

        byte[] signedInfoBytes = objectMapper.writeValueAsBytes(signedInfo);

        return ZipUtils.createZip(file.getOriginalFilename(), fileBytes,
                file.getOriginalFilename() + ".txt", signedBytes,
                "signature.json", signedInfoBytes);
    }

    /**
     * Проверяет цифровую подпись для заданных данных и пользователя.
     *
     * @param userId идентификатор пользователя
     * @return true, если подпись действительна, иначе false
     * @throws NoSuchAlgorithmException если алгоритм подписи не найден
     * @throws InvalidKeyException      если ключ некорректен
     * @throws SignatureException       если произошла ошибка при проверке подписи
     */
    @Override
    public boolean verifySignature(MultipartFile signedFile, UUID userId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {

        // 1. Получаем публичный ключ пользователя
        PublicKey publicKey = keyService.getPublicKey(userId);

        // 2. Извлекаем данные из ZIP
        Map<String, byte[]> zipEntries = ZipUtils.extractZip(signedFile.getBytes());

        byte[] documentBytes = zipEntries.get("document");
        byte[] storedHashBytes = Base64.getDecoder().decode(new String(zipEntries.get("document.hash")));
        byte[] signatureBytes = Base64.getDecoder().decode(new String(zipEntries.get("signature.sig")));

        // 3. Повторны вычисляем хэш документа
        MessageDigest digest = MessageDigest.getInstance(SHA_256);
        byte[] calculatedHash = digest.digest(documentBytes);

        // 4. Проверка целосности документа
        if (MessageDigest.isEqual(storedHashBytes, calculatedHash)) {
            return false;
        }

        // 5. Проверка подписи
        Signature signature = Signature.getInstance(SHA256_WITH_RSA);
        signature.initVerify(publicKey);
        signature.update(calculatedHash);

        return signature.verify(signatureBytes);
    }

    @Override
    public byte[] signData(UUID userId, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return new byte[0];
    }
}
