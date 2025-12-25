package esmukanov.ds.system.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignedInfo {

    private String fileName;
    private String hashAlgorithm;
    private String signatureAlgorithm;
    private String hashFileBase64;
    private LocalDateTime signedAt;
    private String userId;
    private String publicKeyBase64;
}
