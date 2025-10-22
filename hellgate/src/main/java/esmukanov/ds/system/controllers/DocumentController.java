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

    @GetMapping("/user/{userId}/basePath/{basePath}")
    public List<Document> getUserDocuments(@PathVariable UUID userId, @PathVariable String basePath) {
        return documentService.getUserDocuments(userId, basePath);
    }

    @PostMapping("/{username}")
    public String uploadDocument(@RequestBody Document document, @PathVariable String username) {
        return documentService.uploadDocument(document, username);
    }

    @PutMapping("/{documentId}/signature")
    public void attachSignature(@PathVariable UUID documentId, @RequestBody String signatureBase64) {
        documentService.attachSignature(documentId, signatureBase64);
    }
}
