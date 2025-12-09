package com.mobble.mobbleserver.application.image.port.provided;

import com.mobble.mobbleserver.application.image.command.UploadImageCommand;
import com.mobble.mobbleserver.domain.image.Image;

public interface ImageUploadPort {

    Image upload(UploadImageCommand command);
}
