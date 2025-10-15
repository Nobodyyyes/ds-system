package esmukanov.ds.system.controllers;

import esmukanov.ds.system.models.Document;
import esmukanov.ds.system.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/user/{userId}")
    public List<Document> getUserDocuments(@PathVariable UUID userId) {
        return documentService.getUserDocuments(userId);
    }

    @PostMapping
    public Document uploadDocument(@RequestBody Document document) {
        return documentService.uploadDocument(document);
    }

    @PutMapping("/{documentId}/signature")
    public void attachSignature(@PathVariable UUID documentId, @RequestBody String signatureBase64) {
        documentService.attachSignature(documentId, signatureBase64);
    }
}
