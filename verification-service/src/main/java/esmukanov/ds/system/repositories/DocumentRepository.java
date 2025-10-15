package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

    List<DocumentEntity> findAllByOwnerId(UUID ownerId);
}
