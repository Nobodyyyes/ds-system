package esmukanov.ds.system.jobs;

import esmukanov.ds.system.mappers.KeyMapper;
import esmukanov.ds.system.models.UserKey;
import esmukanov.ds.system.repositories.KeyRepository;
import esmukanov.ds.system.services.KeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RotateKeysJob {

    private final KeyService keyService;

    private final KeyRepository keyRepository;

    private final KeyMapper keyMapper;

    @Scheduled(cron = "${cron.rotate-time}")
    public void rotateKeys() {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);

        List<UserKey> keysToRotate = keyMapper.toModels(keyRepository.findAllByIsRevokedFalseAndCreatedAtBefore(monthAgo));
        if (keysToRotate.isEmpty()) {
            return;
        }

        Set<UUID> userIds = keysToRotate
                .stream()
                .map(UserKey::getId)
                .collect(Collectors.toSet());

        for (UUID userId : userIds) {
            try {
                keyService.rotateKey(userId);
            } catch (Exception e) {
                log.error("Failed to rotate key for user [{}]", userId, e);
            }
        }
    }
}
