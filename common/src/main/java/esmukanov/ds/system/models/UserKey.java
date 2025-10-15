package esmukanov.ds.system.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKey {

    private UUID id;

    private UUID userId;

    private String privateKey;

    private String publicKey;
}
