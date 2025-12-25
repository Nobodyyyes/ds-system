package esmukanov.ds.system.utils;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@UtilityClass
public class ZipUtils {

    public static byte[] createZip(String firstFileName, byte[] firstBytes,
                                   String secondFileName, byte[] secondBytes,
                                   String thirdFileName, byte[] thirdBytes) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            addEntry(zos, firstFileName, firstBytes);
            addEntry(zos, secondFileName, secondBytes);
            addEntry(zos, thirdFileName, thirdBytes);

            zos.finish();
            return baos.toByteArray();

        } catch (IOException ioe) {
            throw new RuntimeException("Failed to create ZIP archive", ioe);
        }
    }

    private static void addEntry(ZipOutputStream zos, String fileName, byte[] content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        zos.write(content);
        zos.closeEntry();
    }

    public static Map<String, byte[]> extractZip(byte[] zipBytes) throws IOException {
        Map<String, byte[]> extractedFiles = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];
                int read;
                while ((read = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }

                extractedFiles.put(zipEntry.getName(), baos.toByteArray());
                zis.close();
            }
        }

        return extractedFiles;
    }
}
