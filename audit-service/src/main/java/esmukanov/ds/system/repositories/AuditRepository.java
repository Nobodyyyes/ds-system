package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditEntity, UUID> {

    List<AuditEntity> getAllByUserId(UUID userId);
}
