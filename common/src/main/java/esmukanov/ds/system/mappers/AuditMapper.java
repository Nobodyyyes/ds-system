package esmukanov.ds.system.mappers;

import esmukanov.ds.system.components.base.BaseMapper;
import esmukanov.ds.system.entities.AuditEntity;
import esmukanov.ds.system.models.Audit;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper implements BaseMapper<Audit, AuditEntity> {

    @Override
    public Audit toModel(AuditEntity entity) {
        if (entity == null) return null;

        return Audit.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .action(entity.getAction())
                .timestamp(entity.getTimestamp())
                .details(entity.getDetails())
                .module(entity.getModule())
                .build();
    }

    @Override
    public AuditEntity toEntity(Audit model) {
        if (model == null) return null;

        return AuditEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .action(model.getAction())
                .timestamp(model.getTimestamp())
                .details(model.getDetails())
                .module(model.getModule())
                .build();
    }
}
