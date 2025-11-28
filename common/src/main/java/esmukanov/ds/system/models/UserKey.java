package esmukanov.ds.system.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKey {

    private UUID id;

    private UUID userId;

    private String privateKeyEncrypted;

    private String privateKeyIv;

    private String publicKey;

    private LocalDateTime createdAt;

    private boolean isRevoked;

    private LocalDateTime revokedAt;

    private int version;
}
