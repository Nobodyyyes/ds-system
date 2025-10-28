package esmukanov.ds.system.services;

import esmukanov.ds.system.components.base.BaseCrudOperation;
import esmukanov.ds.system.models.UserKey;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.UUID;

public interface KeyService extends BaseCrudOperation<UserKey, UUID> {

    Optional<UserKey> findKeysByUserId(UUID userId);

    void generateKeyPair(UUID userId) throws NoSuchAlgorithmException;

    PrivateKey getPrivateKey(UUID userId) throws NoSuchAlgorithmException, InvalidKeySpecException;

    PublicKey getPublicKey(UUID userId);

    String getPublicKeyAsString(UUID userId);

    void deleteKeys(UUID userId);
}
