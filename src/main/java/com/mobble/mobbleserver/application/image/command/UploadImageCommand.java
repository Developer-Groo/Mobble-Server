package com.mobble.mobbleserver.application.image.command;

import com.mobble.mobbleserver.domain.image.ImageType;

public record UploadImageCommand(
        String originalName,
        long size,
        String contentType,
        byte[] bytes,
        ImageType type
) {

    public static UploadImageCommand create(
            String originalName,
            long size,
            String contentType,
            byte[] bytes,
            ImageType type
    ) {
        return new UploadImageCommand(
                originalName,
                size,
                contentType,
                bytes,
                type
        );
    }
}
