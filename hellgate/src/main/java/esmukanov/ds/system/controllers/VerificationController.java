package esmukanov.ds.system.controllers;

import esmukanov.ds.system.enums.VerificationStatus;
import esmukanov.ds.system.models.Document;
import esmukanov.ds.system.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/document/signature/{userId}")
    public VerificationStatus verifyDocumentSignature(@RequestBody Document document,
                                                      @RequestBody byte[] signatureBytes,
                                                      @PathVariable UUID userId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        return verificationService.verifyDocument(document, signatureBytes, userId);
    }
}
