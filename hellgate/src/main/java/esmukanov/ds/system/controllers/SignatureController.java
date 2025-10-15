package esmukanov.ds.system.controllers;

import esmukanov.ds.system.services.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@RestController
@RequestMapping("/api/signature")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;

    @PostMapping("/sign/{userId}")
    public byte[] signData(@RequestBody byte[] data, @PathVariable UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        return signatureService.signData(data, userId);
    }

    @PostMapping("/verify/{userId}")
    public boolean verifySignature(@RequestBody byte[] data,
                                   @RequestBody byte[] signatureBytes,
                                   @PathVariable UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        return signatureService.verifySignature(data, signatureBytes, userId);
    }
}
