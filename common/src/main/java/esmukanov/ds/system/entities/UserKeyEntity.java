package esmukanov.ds.system.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "private_key", length = 4096)
    private String privateKey;

    @Column(name = "public_key", length = 4096)
    private String publicKey;
}
