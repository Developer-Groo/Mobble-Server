package com.mobble.mobbleserver.infrastructure.storage.s3;

import com.mobble.mobbleserver.application.image.port.required.ImageStoragePort;
import org.springframework.stereotype.Component;

@Component
public class S3ImageStorageAdapter implements ImageStoragePort {

    @Override
    public String upload(byte[] bytes, String path, String fileName, String contentType) {
        return "";
    }

    @Override
    public void delete(String urlOrKey) {

    }
}
