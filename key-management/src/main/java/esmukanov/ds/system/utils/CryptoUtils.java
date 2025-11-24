package esmukanov.ds.system.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class CryptoUtils {

    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Шифрует данные с помощью AES в режиме GCM.
     *
     * @param plain исходные данные в виде массива байт
     * @param key   секретный ключ AES
     * @return объект EncryptedData, содержащий IV и зашифрованный текст в Base64
     * @throws RuntimeException если возникает ошибка шифрования
     */
    public static EncryptedData encryptAESGCM(byte[] plain, SecretKey key) {
        byte[] iv = new byte[12];
        SECURE_RANDOM.nextBytes(iv);
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] cipherText = cipher.doFinal(plain);
            return new EncryptedData(Base64.getEncoder().encodeToString(iv), Base64.getEncoder().encodeToString(cipherText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Расшифровывает данные, зашифрованные с помощью AES в режиме GCM.
     *
     * @param data объект EncryptedData, содержащий IV и зашифрованный текст в Base64
     * @param key  секретный ключ AES
     * @return расшифрованные данные в виде массива байт
     * @throws RuntimeException если возникает ошибка расшифровки
     */
    public static byte[] decryptAESGCM(EncryptedData data, SecretKey key) {
        try {
            byte[] iv = Base64.getDecoder().decode(data.iv());
            byte[] cipherText = Base64.getDecoder().decode(data.cipherText());
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получает мастер-ключ AES из строки в формате Base64.
     *
     * @param base64 строка с закодированным в Base64 ключом
     * @return объект SecretKey для алгоритма AES
     * @throws IllegalArgumentException если строка не является корректной Base64
     */
    public static SecretKey getMasterKeyFromEnv(String base64) {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        return new SecretKeySpec(keyBytes, AES);
    }

    /**
     * Запись для хранения зашифрованных данных: IV и шифротекста в формате Base64.
     *
     * @param iv         инициализационный вектор (IV) в Base64
     * @param cipherText зашифрованный текст в Base64
     */
    public record EncryptedData(String iv, String cipherText) {
    }
}
