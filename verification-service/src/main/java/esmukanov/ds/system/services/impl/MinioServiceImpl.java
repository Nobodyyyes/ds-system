package esmukanov.ds.system.services.impl;

import esmukanov.ds.system.exceptions.MinioFileException;
import esmukanov.ds.system.models.Attachment;
import esmukanov.ds.system.properties.MinioProperties;
import esmukanov.ds.system.services.MinioService;
import esmukanov.ds.system.utils.MinioUtils;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    private final String bucket;

    public MinioServiceImpl(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.bucket = minioProperties.getBucket();
    }

    /**
     * Загружает список файлов в MinIO для указанного пользователя.
     *
     * @param username имя пользователя, для которого загружаются файлы
     * @param files    список файлов для загрузки
     * @return базовый путь, по которому были сохранены файлы
     * @throws IllegalStateException если произошла ошибка при загрузке файла
     */
    @Override
    public String upload(String username, List<MultipartFile> files) {
        String basePath = MinioUtils.generatePath(username);

        for (MultipartFile file : files) {
            String objectName = basePath + "/" + file.getOriginalFilename();
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(inputStream, files.size(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            } catch (Exception e) {
                throw new MinioFileException("Ошибка при загрузке файла в MinIO: " + objectName, e);
            }
        }

        return basePath;
    }

    /**
     * Получает список вложений (файлов) из MinIO по указанному базовому пути.
     *
     * @param basePah базовый путь, по которому хранятся файлы
     * @return список объектов Attachment, содержащих имя файла, содержимое и тип контента
     * @throws MinioFileException если произошла ошибка при получении файлов из MinIO
     */
    @Override
    public List<Attachment> getAttachment(String basePah) {
        List<Attachment> attachments = new ArrayList<>();
        Iterable<Result<Item>> results = listObjects(basePah);

        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();

                try (InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .build()
                )) {
                    byte[] content = stream.readAllBytes();
                    Path path = Paths.get(objectName);
                    String contentType = Files.probeContentType(path);

                    attachments.add(new Attachment()
                            .setFileName(path.getFileName().toString())
                            .setFileContent(content)
                            .setContentType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE));
                }
            }
        } catch (Exception e) {
            throw new MinioFileException("Ошибка при получении файлов из MinIO", e);
        }

        return attachments;
    }

    /**
     * Возвращает итератор объектов MinIO по заданному базовому пути.
     *
     * @param basePath базовый путь, по которому ищутся объекты
     * @return итератор результатов объектов MinIO
     * @throws MinioFileException если произошла ошибка при получении объектов из MinIO
     */
    private Iterable<Result<Item>> listObjects(String basePath) {

        try {
            return minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(MinioUtils.normalizedPathPrefix(basePath))
                            .recursive(true)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioFileException("Ошибка получения файлов из MinIO по пути: [%s]".formatted(basePath), e);
        }
    }
}
