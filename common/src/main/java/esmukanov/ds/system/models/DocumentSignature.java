package esmukanov.ds.system.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSignature {

    private UUID id;

    private UUID userId;

    private String fileName;

    private String fileHash;

    private UUID signatureId;

    private String signature;

    private LocalDateTime signedDate;
}
