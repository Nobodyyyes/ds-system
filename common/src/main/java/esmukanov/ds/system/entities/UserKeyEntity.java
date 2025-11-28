package esmukanov.ds.system.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_keys")
public class UserKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "private_key_encrypted", length = 4096)
    private String privateKeyEncrypted;

    @Column(name = "private_key_iv", length = 4096)
    private String privateKeyIv;

    @Column(name = "public_key", length = 4096)
    private String publicKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "revoked")
    private boolean isRevoked;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "version")
    private int version;
}
