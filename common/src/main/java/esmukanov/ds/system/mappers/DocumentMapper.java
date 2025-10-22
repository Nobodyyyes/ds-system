package esmukanov.ds.system.mappers;

import esmukanov.ds.system.components.base.BaseMapper;
import esmukanov.ds.system.entities.DocumentEntity;
import esmukanov.ds.system.models.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper implements BaseMapper<Document, DocumentEntity> {

    @Override
    public Document toModel(DocumentEntity entity) {
        if (entity == null) return null;

        return Document.builder()
                .id(entity.getId())
                .fileName(entity.getName())
                .filePath(entity.getFilePath())
                .uploadDate(entity.getUploadDate())
                .signedDate(entity.getSignedDate())
                .ownerId(entity.getOwnerId())
                .signature(entity.getSignature())
                .build();
    }

    @Override
    public DocumentEntity toEntity(Document model) {
        if (model == null) return null;

        return DocumentEntity.builder()
                .id(model.getId())
                .name(model.getFile().getOriginalFilename())
                .type(model.getFile().getContentType())
                .filePath(model.getFilePath())
                .uploadDate(model.getUploadDate())
                .signedDate(model.getSignedDate())
                .ownerId(model.getOwnerId())
                .signature(model.getSignature())
                .build();
    }
}
