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
@Table(name = "document_signature")
public class DocumentSignatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_hash", length = 4096)
    private String fileHash;

    @Column(name = "signature_id")
    private UUID signatureId;

    @Column(name = "signature", length = 4096)
    private String signature;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;
}
