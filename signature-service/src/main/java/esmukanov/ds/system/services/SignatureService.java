package esmukanov.ds.system.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public interface SignatureService {

    String signDocument(MultipartFile file, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException;

    boolean verifySignature(byte[] data, byte[] signatureBytes, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException;

    byte[] signData(UUID userId, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;

}
