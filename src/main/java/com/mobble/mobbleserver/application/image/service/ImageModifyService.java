package com.mobble.mobbleserver.application.image.service;

import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.image.command.UploadImageCommand;
import com.mobble.mobbleserver.application.image.error.ImageBusinessError;
import com.mobble.mobbleserver.application.image.port.provided.ImageDeletePort;
import com.mobble.mobbleserver.application.image.port.provided.ImageUploadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageStoragePort;
import com.mobble.mobbleserver.application.image.port.required.ImageWritePort;
import com.mobble.mobbleserver.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageModifyService implements ImageUploadPort, ImageDeletePort {

    private final ImageStoragePort imageStoragePort;

    private final ImageWritePort imageWritePort;

    private final ImageReadPort imageReadPort;

    @Override
    public Image upload(UploadImageCommand command) {
        assertValidFile(command);

        String extracted = extractExtension(command.originalName());
        String storedFileName = UUID.randomUUID() + extracted;
        String path = command.type().getPath();

        String url = imageStoragePort.upload(
                command.bytes(),
                path,
                storedFileName,
                command.contentType()
        );

        Image image = Image.create(
                url,
                command.originalName(),
                command.size(),
                command.contentType(),
                command.type()
        );

        return imageWritePort.save(image);
    }

    @Override
    public void delete(Long imageId) {
        Image image = assertImageByImageId(imageId);

        if (image.isDefault()) return;

        imageStoragePort.delete(image.getUrl());
        imageWritePort.delete(image);
    }

    @Override
    public void deleteAll(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) return;

        List<Long> distinctIds = imageIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (distinctIds.isEmpty()) return;

        List<Image> images = imageReadPort.findAllByIds(distinctIds);

        if (images.isEmpty()) return;

        List<Image> deletableImages = images.stream()
                .filter(image -> !image.isDefault())
                .toList();

        if (deletableImages.isEmpty()) return;

        images.forEach(image -> imageStoragePort.delete(image.getUrl()));

        imageWritePort.deleteAll(images);
    }

    /* ==== Private Helper ==== */
    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private void assertValidFile(UploadImageCommand command) {
        if (command.size() <= 0) throw new BusinessException(ImageBusinessError.INVALID_IMAGE_SIZE);

        // 5MB 제한
        long maxSize = 5L * 1024 * 1024;
        if (command.size() > maxSize) throw new BusinessException(ImageBusinessError.INVALID_IMAGE_SIZE);

        if (command.contentType() == null || !command.contentType().startsWith("image/"))
            throw new BusinessException(ImageBusinessError.INVALID_IMAGE_TYPE);
    }

    private String extractExtension(String originalName) {
        if (originalName == null) return "";

        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex == -1) return "";

        return originalName.substring(dotIndex);
    }
}
