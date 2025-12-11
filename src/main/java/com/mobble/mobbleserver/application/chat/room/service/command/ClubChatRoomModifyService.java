package com.mobble.mobbleserver.application.chat.room.service.command;

import com.mobble.mobbleserver.application.chat.room.error.ChatRoomBusinessError;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.command.club.ClubChatRoomJoinPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;
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
    public void create(Long clubId, Long memberId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        clubMember.assertApproved();

        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        assertClubChatRoomNotExists(club.getId());

        ChatRoom clubChatRoom = ChatRoom.createClub(club);

        clubChatRoom.addParticipant(member);
        chatRoomWritePort.save(clubChatRoom);
    }

    @Override
    public void join(Long clubId, Long memberId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        clubMember.assertApproved();

        Member member = clubMember.getMember();
        Club club = clubMember.getClub();

        ClubRoomInfo clubRoomInfo = assertClubRoomInfoByClubId(club.getId());

        ChatRoom chatRoom = clubRoomInfo.getChatRoom();

        chatRoom.addParticipant(member);
    }

    /* ==== Private Helper ==== */
    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB));
    }

    private ClubRoomInfo assertClubRoomInfoByClubId(Long clubId) {
        return chatRoomReadPort.findClubRoomInfoByClubId(clubId)
                .orElseThrow(() -> new BusinessException(ChatRoomBusinessError.NOT_FOUND));
    }

    private void assertClubChatRoomNotExists(Long clubId) {
        if (chatRoomReadPort.existsClubRoomInfo(clubId)) throw new BusinessException(ChatRoomBusinessError.ALREADY_EXISTS);
    }
}
