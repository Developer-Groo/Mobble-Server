package com.mobble.mobbleserver.application.image.port.required;

import com.mobble.mobbleserver.domain.image.Image;

import java.util.List;

public interface ImageWritePort {

    Image save(Image image);

    void delete(Image image);

    void deleteAll(List<Image> images);
}
