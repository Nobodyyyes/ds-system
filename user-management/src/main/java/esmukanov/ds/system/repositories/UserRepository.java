package esmukanov.ds.system.repositories;

import esmukanov.ds.system.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);
}
