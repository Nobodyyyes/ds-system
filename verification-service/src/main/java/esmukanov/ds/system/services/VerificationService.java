package esmukanov.ds.system.services;

import esmukanov.ds.system.models.Document;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public interface VerificationService {

    boolean verifyDocumentSignature(Document document, byte[] signatureBytes, UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException;
}
