package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.UserKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepository extends JpaRepository<UserKeyEntity, UUID> {

    Optional<UserKeyEntity> findByUserId(UUID userId);

    List<UserKeyEntity> findAllByUserId(UUID userId);

    List<UserKeyEntity> findAllByIsRevokedFalseAndCreatedAtBefore(LocalDateTime createdAtBefore);

    void deleteAllByUserId(UUID userId);

    @Query("select coalesce(max(uk.version), 0) from UserKeyEntity uk where uk.userId = :userId")
    int findCurrentVersion(@Param("userId") UUID userId);
}
