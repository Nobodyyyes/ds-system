package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.entities.DocumentEntity;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.DocumentMapper;
import esmukanov.ds.system.models.Document;
import esmukanov.ds.system.repositories.DocumentRepository;
import esmukanov.ds.system.services.DocumentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl extends BaseCrudOperationImpl<Document, DocumentEntity, UUID> implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        super(documentRepository, documentMapper);
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * Возвращает список документов пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return список моделей документов, принадлежащих пользователю
     */
    @Override
    public List<Document> getUserDocuments(UUID userId) {
        return documentMapper.toModels(documentRepository.findAllByOwnerId(userId));
    }

    /**
     * Загружает новый документ.
     *
     * @param document модель документа для загрузки
     * @return сохранённая модель документа с установленной датой загрузки
     */
    @Override
    public Document uploadDocument(Document document) {
        document.setUploadDate(LocalDateTime.now());
        return documentMapper.toModel(documentRepository.save(documentMapper.toEntity(document)));
    }

    /**
     * Прикрепляет электронную подпись к документу.
     *
     * @param documentId      идентификатор документа
     * @param signatureBase64 электронная подпись в формате Base64
     * @throws NotFoundException если документ с указанным идентификатором не найден
     */
    @Override
    public void attachSignature(UUID documentId, String signatureBase64) {
        Document document = documentRepository.findById(documentId)
                .map(documentMapper::toModel)
                .orElseThrow(() -> new NotFoundException("Document by ID [%s] not found".formatted(documentId)));

        document.setSignature(signatureBase64);
        document.setSignedDate(LocalDateTime.now());
        documentRepository.save(documentMapper.toEntity(document));
    }
}
