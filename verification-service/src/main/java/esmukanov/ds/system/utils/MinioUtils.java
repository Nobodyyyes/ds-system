package esmukanov.ds.system.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@UtilityClass
public class MinioUtils {

    public static String generatePath(String username) {
        String uuid = UUID.randomUUID().toString();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
        return String.format("%s/%s/%s", username, uuid, date);
    }
}
