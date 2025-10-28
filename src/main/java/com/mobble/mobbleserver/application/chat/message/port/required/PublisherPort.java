package com.mobble.mobbleserver.application.chat.message.port.required;

public interface PublisherPort {

    void publishToRoom(Long chatRoomId, Object payload);
}
