package esmukanov.ds.system.mappers;

import esmukanov.ds.system.components.base.BaseMapper;
import esmukanov.ds.system.entities.DocumentSignatureEntity;
import esmukanov.ds.system.models.DocumentSignature;
import org.springframework.stereotype.Component;

@Component
public class DocumentSignatureMapper implements BaseMapper<DocumentSignature, DocumentSignatureEntity> {

    @Override
    public DocumentSignature toModel(DocumentSignatureEntity entity) {
        if (entity == null) return null;

        return DocumentSignature.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .fileName(entity.getFileName())
                .fileHash(entity.getFileHash())
                .signatureId(entity.getSignatureId())
                .signature(entity.getSignature())
                .signedDate(entity.getSignedDate())
                .build();
    }

    @Override
    public DocumentSignatureEntity toEntity(DocumentSignature model) {
        if (model == null) return null;

        return DocumentSignatureEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .fileName(model.getFileName())
                .fileHash(model.getFileHash())
                .signatureId(model.getSignatureId())
                .signature(model.getSignature())
                .signedDate(model.getSignedDate())
                .build();
    }
}
