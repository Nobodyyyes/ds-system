package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.DocumentSignatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentSignatureRepository extends JpaRepository<DocumentSignatureEntity, UUID> {
}
