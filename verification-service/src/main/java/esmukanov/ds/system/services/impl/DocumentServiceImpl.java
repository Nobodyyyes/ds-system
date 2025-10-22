package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.components.base.BaseCrudOperationImpl;
import esmukanov.ds.system.entities.DocumentEntity;
import esmukanov.ds.system.exceptions.NotFoundException;
import esmukanov.ds.system.mappers.DocumentMapper;
import esmukanov.ds.system.models.Attachment;
import esmukanov.ds.system.models.Document;
import esmukanov.ds.system.repositories.DocumentRepository;
import esmukanov.ds.system.services.DocumentService;
import esmukanov.ds.system.services.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl extends BaseCrudOperationImpl<Document, DocumentEntity, UUID> implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final MinioService minioService;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper, MinioService minioService) {
        super(documentRepository, documentMapper);
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.minioService = minioService;
    }

    /**
     * Возвращает список документов пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return список моделей документов, принадлежащих пользователю
     */
    @Override
    public List<Document> getUserDocuments(UUID userId, String basePath) {
        List<Attachment> attachments = minioService.getAttachment(basePath);

        List<Document> documents = documentMapper.toModels(documentRepository.findAllByOwnerId(userId));
        if (documents.isEmpty()) {
            throw new NotFoundException(String.format("No documents found for userId [%s]", userId));
        }

        for (Document document : documents) {
            attachments.stream()
                    .filter(a -> a.getFileName().equals(document.getFileName()))
                    .findFirst();
        }

        return documents;
    }

    /**
     * Загружает новый документ.
     *
     * @param document модель документа для загрузки
     * @return сохранённая модель документа с установленной датой загрузки
     */
    @Override
    public String uploadDocument(Document document, String username) {
        if (document == null || document.getFile().isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        List<MultipartFile> files = List.of(document.getFile());
        String uploadedPath = minioService.upload(username, files);

        document.setUploadDate(LocalDateTime.now());
        document.setFilePath(uploadedPath);

        documentRepository.save(documentMapper.toEntity(document));

        return uploadedPath;
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
