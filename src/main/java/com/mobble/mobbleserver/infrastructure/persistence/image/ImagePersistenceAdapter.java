package com.mobble.mobbleserver.infrastructure.persistence.image;

import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageWritePort;
import com.mobble.mobbleserver.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImagePersistenceAdapter implements ImageReadPort, ImageWritePort {

    private final JpaImageRepository repository;

    @Override
    public Optional<Image> findById(Long id) {
        return repository.findById(id);
    }
}
