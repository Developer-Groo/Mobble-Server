package com.mobble.mobbleserver.infrastructure.persistence.image;

import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageWritePort;
import com.mobble.mobbleserver.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImagePersistenceAdapter implements ImageReadPort, ImageWritePort {

    private final JpaImageRepository repository;

    /* ImageWritePort */
    @Override
    public Image save(Image image) {
        return repository.save(image);
    }

    @Override
    public void delete(Image image) {
        repository.delete(image);
    }

    @Override
    public void deleteAll(List<Image> images) {
        repository.deleteAll(images);
    }

    /* ImageReadPort */
    @Override
    public Optional<Image> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Image> findAllByIds(List<Long> imageIds) {
        return repository.findAllById(imageIds);
    }
}
