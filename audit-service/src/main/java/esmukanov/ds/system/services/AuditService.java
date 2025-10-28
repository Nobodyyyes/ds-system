package esmukanov.ds.system.services;

import esmukanov.ds.system.enums.Module;
import esmukanov.ds.system.models.Audit;

import java.util.List;
import java.util.UUID;

public interface AuditService {

    void logAction(UUID userId, String action, String details, Module module);

    List<Audit> getUserHistory(UUID userId);

    List<Audit> getAllActions();
}
