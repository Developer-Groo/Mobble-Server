package com.mobble.mobbleserver.application.image.port.required;

public interface ImageStoragePort {

    String upload(byte[] bytes, String path, String fileName, String contentType);

    void delete(String urlOrKey);
}
