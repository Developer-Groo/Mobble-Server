package com.mobble.mobbleserver.application.chat.room.port.provided.command.participant;

public interface ParticipantUpdatePort {

    void updateLastReadMessage(Long chatRoomId, Long memberId, Long lastMessageId);

    void updateNotification(Long chatRoomId, Long memberId, boolean enabled);
}
