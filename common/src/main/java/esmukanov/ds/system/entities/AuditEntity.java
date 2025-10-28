package esmukanov.ds.system.entities;

import esmukanov.ds.system.enums.Module;
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
@Table(name = "audits")
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "action", length = 2000)
    private String action;

    @Column(name = "action_date")
    private LocalDateTime timestamp;

    @Column(name = "details", length = 2000)
    private String details;

    @Column(name = "module")
    @Enumerated(EnumType.STRING)
    private Module module;
}
