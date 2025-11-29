package com.mobble.mobbleserver.infrastructure.web.image;

import com.mobble.mobbleserver.application.image.command.UploadImageCommand;
import com.mobble.mobbleserver.application.image.port.provided.ImageDeletePort;
import com.mobble.mobbleserver.application.image.port.provided.ImageUploadPort;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;
import com.mobble.mobbleserver.infrastructure.web.image.dto.response.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageAPI {

    private final ImageUploadPort imageUploadPort;
    private final ImageDeletePort imageDeletePort;

    @PostMapping
    public ResponseEntity<ImageResponseDto> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("type") ImageType type
    ) throws IOException {
        UploadImageCommand command = UploadImageCommand.create(file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes(), type);

        Image image = imageUploadPort.upload(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImageResponseDto.toDto(image));
    }

    @DeleteMapping("/{image-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("image-id") Long imageId
    ) {
        imageDeletePort.delete(imageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
