package esmukanov.ds.system.services;

import esmukanov.ds.system.components.base.BaseCrudOperation;
import esmukanov.ds.system.models.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService extends BaseCrudOperation<Document, UUID> {

    List<Document> getUserDocuments(UUID userId, String basePath);

    String uploadDocument(Document document, String username);

    void attachSignature(UUID documentId, String signatureBase64);
}
