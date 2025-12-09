package com.mobble.mobbleserver.infrastructure.persistence.image;

import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByTypeAndIsDefault(ImageType imageType, boolean isDefault);
}
