package esmukanov.ds.system.controllers;

import esmukanov.ds.system.services.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(
            path = "/sign/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> signData(@RequestPart("file") MultipartFile file,
                                           @PathVariable UUID userId) throws Exception {

        byte[] zipBytes = signatureService.signDocument(file, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"signed-document.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
    }

    @PostMapping(value = "/verify/{userId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean verifySignature(@RequestPart("signedFile") MultipartFile signedFile,
                                   @PathVariable UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, IOException {
        return signatureService.verifySignature(signedFile, userId);
    }
}
