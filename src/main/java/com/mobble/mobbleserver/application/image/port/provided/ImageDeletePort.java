package com.mobble.mobbleserver.application.image.port.provided;

import java.util.List;

public interface ImageDeletePort {

    void delete(Long imageId);

    void deleteAll(List<Long> imageIds);
}
