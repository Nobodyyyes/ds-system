package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.services.MinioService;
import esmukanov.ds.system.utils.MinioUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MinioServiceImpl implements MinioService {

    @Override
    public String uploadBucket(String username, List<MultipartFile> files) {
        String basePath = MinioUtils.generatePath(username);
        return basePath;
    }
}
