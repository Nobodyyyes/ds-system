package esmukanov.ds.system.configurations.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@ConfigurationProperties(prefix = "minio-config")
@Getter
@Setter
public class MinioProperties {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;
}
