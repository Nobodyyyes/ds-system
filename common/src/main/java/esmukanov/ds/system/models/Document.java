package esmukanov.ds.system.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    private UUID id;

    private String name;

    private String type;

    private String filePath;

    private LocalDateTime uploadDate;

    private LocalDateTime signedDate;

    private UUID ownerId;

    private String signature;
}
