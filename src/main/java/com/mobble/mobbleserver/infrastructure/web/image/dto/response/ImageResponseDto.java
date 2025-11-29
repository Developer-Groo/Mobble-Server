package com.mobble.mobbleserver.infrastructure.web.image.dto.response;

import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;

public record ImageResponseDto(
        Long imageId,
        String url,
        String originalName,
        long size,
        String contentType,
        ImageType type
) {

    public static ImageResponseDto toDto(Image image) {
        return new ImageResponseDto(
                image.getId(),
                image.getUrl(),
                image.getOriginalName(),
                image.getSize(),
                image.getContentType(),
                image.getType()
        );
    }
}
