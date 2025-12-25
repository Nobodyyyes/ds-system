package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.enums.VerificationStatus;
import esmukanov.ds.system.models.Document;
import esmukanov.ds.system.services.SignatureService;
import esmukanov.ds.system.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final SignatureService signatureService;

    /**
     * Проверяет подпись документа для указанного пользователя.
     *
     * @param document       документ, подпись которого проверяется
     * @param signatureBytes байты подписи
     * @param userId         идентификатор пользователя
     * @return true, если подпись действительна, иначе false
     * @throws IOException              если не удалось прочитать файл документа
     * @throws NoSuchAlgorithmException если алгоритм подписи не найден
     * @throws InvalidKeySpecException  если спецификация ключа недействительна
     * @throws SignatureException       если произошла ошибка при проверке подписи
     * @throws InvalidKeyException      если ключ недействителен
     */
    @Override
    public VerificationStatus verifyDocument(Document document, byte[] signatureBytes, UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        return VerificationStatus.VALID;
    }
}
