package esmukanov.ds.system.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public interface SignatureService {

    byte[] signData(byte[] data, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException;

    boolean verifySignature(byte[] data, byte[] signatureBytes, UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException;
}
