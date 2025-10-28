package esmukanov.ds.system.models;

import esmukanov.ds.system.enums.Module;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    private UUID id;

    private UUID userId;

    private String action;

    private LocalDateTime timestamp;

    private String details;

    private Module module;
}
