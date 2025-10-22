package esmukanov.ds.system.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    private UUID id;

    private String fileName;

    private String filePath;

    private LocalDateTime uploadDate;

    private LocalDateTime signedDate;

    private UUID ownerId;

    private String signature;

    private MultipartFile file;
}
