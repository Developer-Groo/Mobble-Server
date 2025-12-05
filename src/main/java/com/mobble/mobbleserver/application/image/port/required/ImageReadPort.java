package com.mobble.mobbleserver.application.image.port.required;

import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;

import java.util.List;
import java.util.Optional;

public interface ImageReadPort {

    Optional<Image> findById(Long id);

    List<Image> findAllByIds(List<Long> imageIds);

    Optional<Image> findDefaultByType(ImageType imageType);
}
