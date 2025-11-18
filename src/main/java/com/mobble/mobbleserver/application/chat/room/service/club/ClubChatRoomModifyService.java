package com.mobble.mobbleserver.application.chat.room.service.club;

import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomJoinPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response.ClubChatRoomPreviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubChatRoomModifyService implements ClubChatRoomCreatePort, ClubChatRoomJoinPort {

    private final ChatRoomWritePort chatRoomWritePort;

    private final ChatRoomReadPort chatRoomReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    @Override
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId).orElseThrow();
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        if (chatRoomReadPort.existsClubRoomInfo(clubId)) throw new IllegalStateException();

        ChatRoom clubChatRoom = ChatRoom.createClub(club);

        clubChatRoom.addParticipant(member);
        chatRoomWritePort.save(clubChatRoom);

        return ClubChatRoomPreviewResponseDto.toDto(clubChatRoom, club, null, 0, null);
    }

    @Override
    public void joinClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId).orElseThrow();
        Member member = clubMember.getMember();
        Club club = clubMember.getClub();

        ClubRoomInfo clubRoomInfo = chatRoomReadPort.findClubRoomInfoByClubId(club.getId()).orElseThrow();

        ChatRoom chatRoom = clubRoomInfo.getChatRoom();

        chatRoom.addParticipant(member);
    }
}
