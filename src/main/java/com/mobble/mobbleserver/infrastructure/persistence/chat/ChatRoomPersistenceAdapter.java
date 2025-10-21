package com.mobble.mobbleserver.infrastructure.persistence.chat;

import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.infrastructure.persistence.chat.club.ClubRoomInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomWritePort, ChatRoomReadPort {

    private final ClubRoomInfoRepository clubRoomInfoRepository;

    /* ChatRoomWritePort-Club */
    @Override
    public boolean existsClubRoomInfoByClubId(Long clubId) {
        return clubRoomInfoRepository.existsClubRoomInfoByClubId(clubId);
    }


    /* ChatRoomReadPort */


}
