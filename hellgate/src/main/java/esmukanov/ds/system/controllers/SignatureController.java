package esmukanov.ds.system.controllers;

import esmukanov.ds.system.services.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(path = "/sign/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String signData(@RequestPart("file") MultipartFile file, @PathVariable UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, IOException {
        return signatureService.signDocument(file, userId);
    }

    @PostMapping("/verify/{userId}")
    public boolean verifySignature(@RequestBody byte[] data,
                                   @RequestBody byte[] signatureBytes,
                                   @PathVariable UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        return signatureService.verifySignature(data, signatureBytes, userId);
    }
}
