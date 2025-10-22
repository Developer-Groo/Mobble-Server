package com.mobble.mobbleserver.infrastructure.persistence.chat;

import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.infrastructure.persistence.chat.club.ClubRoomInfoRepository;
import com.mobble.mobbleserver.infrastructure.persistence.chat.common.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomWritePort, ChatRoomReadPort {

    private final ChatRoomRepository chatRoomRepository;
    private final ClubRoomInfoRepository clubRoomInfoRepository;

    /* ChatRoomWritePort */
    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public void delete(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }

    /* ChatRoomReadPort */
    @Override
    public Optional<ChatRoom> findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    /* ChatRoomReadPort-Club */
    @Override
    public boolean existsClubRoomInfo(Long clubId) {
        return clubRoomInfoRepository.existsClubRoomInfoByClubId(clubId);
    }
}
