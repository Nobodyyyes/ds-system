package esmukanov.ds.system.services;

import esmukanov.ds.system.models.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {

    String upload(String username, List<MultipartFile> files);

    List<Attachment> getAttachment(String basePah);
}
