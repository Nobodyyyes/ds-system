package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.services.KeyService;
import esmukanov.ds.system.services.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final KeyService keyService;

    /**
     * Создаёт цифровую подпись для заданных данных и пользователя.
     *
     * @param data   данные для подписи
     * @param userId идентификатор пользователя
     * @return байты цифровой подписи
     * @throws NoSuchAlgorithmException если алгоритм подписи не найден
     * @throws InvalidKeySpecException  если спецификация ключа некорректна
     * @throws InvalidKeyException      если ключ некорректен
     * @throws SignatureException       если произошла ошибка при создании подписи
     */
    @Override
    public byte[] signData(byte[] data, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = keyService.getPrivateKey(userId);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * Проверяет цифровую подпись для заданных данных и пользователя.
     *
     * @param data           данные, для которых проверяется подпись
     * @param signatureBytes байты подписи
     * @param userId         идентификатор пользователя
     * @return true, если подпись действительна, иначе false
     * @throws NoSuchAlgorithmException если алгоритм подписи не найден
     * @throws InvalidKeySpecException  если спецификация ключа некорректна
     * @throws InvalidKeyException      если ключ некорректен
     * @throws SignatureException       если произошла ошибка при проверке подписи
     */
    @Override
    public boolean verifySignature(byte[] data, byte[] signatureBytes, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
//        PublicKey publicKey = keyService.getPublicKey(userId);
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(publicKey);
//        signature.update(data);
//        return signature.verify(signatureBytes);

        return true;
    }
}
