package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.enums.Module;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.AuditMapper;
import esmukanov.ds.system.models.Audit;
import esmukanov.ds.system.repositories.AuditRepository;
import esmukanov.ds.system.services.AuditService;
import esmukanov.ds.system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    private final AuditMapper auditMapper;

    private final UserService userService;

    /**
     * Записывает действие пользователя в аудит.
     * Создаёт объект Audit с текущим локальным временем и сохраняет его в репозитории.
     *
     * @param userId  идентификатор пользователя
     * @param action  краткое описание действия
     * @param details дополнительные сведения о действии (может быть null)
     * @param module  модуль, в котором произошло действие
     */
    @Override
    public void logAction(UUID userId, String action, String details, Module module) {
        Audit audit = Audit.builder()
                .userId(userId)
                .action(action)
                .timestamp(LocalDateTime.now())
                .details(details)
                .module(module)
                .build();

        auditRepository.save(auditMapper.toEntity(audit));
    }

    /**
     * Возвращает историю действий пользователя.
     * Проверяет существование пользователя и при отсутствии выбрасывает исключение.
     *
     * @param userId идентификатор пользователя
     * @return список записей аудита пользователя (может быть пустым)
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public List<Audit> getUserHistory(UUID userId) {
        if (userService.existsUser(userId)) throw new NotFoundException("User by ID [%s] not found".formatted(userId));

        return auditMapper.toModels(auditRepository.getAllByUserId(userId));
    }

    /**
     * Возвращает список всех записей аудита.
     * Использует репозиторий для получения всех сущностей и преобразует их в модели.
     *
     * @return список всех действий (может быть пустым)
     */
    @Override
    public List<Audit> getAllActions() {
        return auditMapper.toModels(auditRepository.findAll());
    }
}
