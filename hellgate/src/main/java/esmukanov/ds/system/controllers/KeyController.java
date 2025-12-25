package esmukanov.ds.system.controllers;

import esmukanov.ds.system.dtos.PublicKeyDto;
import esmukanov.ds.system.services.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
public class KeyController {

    private final KeyService keyService;

    @PostMapping("/pair/generate/{userId}")
    public void generateKeyPair(@PathVariable UUID userId) throws NoSuchAlgorithmException, IllegalAccessException {
        keyService.generateKeyPair(userId);
    }

    @PostMapping("/rotate/{userId}")
    public PublicKeyDto rotateKeys(@PathVariable UUID userId) {
        return keyService.rotateKey(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteKeys(@PathVariable UUID userId) {
        keyService.deleteKeys(userId);
    }
}
