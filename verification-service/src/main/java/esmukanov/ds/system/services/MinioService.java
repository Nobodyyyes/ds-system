package esmukanov.ds.system.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {

    String uploadBucket(String username, List<MultipartFile> files);
}
