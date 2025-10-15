package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.UserKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KeyRepository extends JpaRepository<UserKeyEntity, UUID> {

    Optional<UserKeyEntity> findByUserId(UUID userId);
}
