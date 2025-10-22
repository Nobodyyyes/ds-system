package esmukanov.ds.system.configurations;

import esmukanov.ds.system.configurations.properties.MinioProperties;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getUrl())
                .build();
    }
}
