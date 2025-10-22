package com.mobble.mobbleserver.application.chat.room.port.provided.club;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;

public interface ClubChatRoomJoinPort {

    void joinClubChatRoom(Long clubId, Long memberId);
}
