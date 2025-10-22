package esmukanov.ds.system.exceptions;

public class MinioFileException extends RuntimeException {

    public MinioFileException(String message) {
        super(message);
    }

    public MinioFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
