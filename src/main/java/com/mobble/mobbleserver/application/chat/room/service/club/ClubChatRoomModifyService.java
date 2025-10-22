package com.mobble.mobbleserver.application.chat.room.service.club;

import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomJoinPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.persistence.chat.common.ChatRoomRepository;
import com.mobble.mobbleserver.infrastructure.persistence.chat.common.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubChatRoomModifyService implements ClubChatRoomCreatePort, ClubChatRoomJoinPort {

    private final ChatRoomReadPort chatRoomReadPort;

    private final ClubMemberValidator clubMemberValidator;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Override
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        if (chatRoomReadPort.existsClubRoomInfo(clubId)) throw new IllegalStateException();

        ChatRoom clubChatRoom = ChatRoom.createClub(club);

        clubChatRoom.addParticipant(member);
        chatRoomRepository.save(clubChatRoom);

        return ClubChatRoomPreviewResponseDto.toDto(clubChatRoom, club, null, 0, null);
    }

    @Override
    public void joinClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Member member = clubMember.getMember();
        Club club = clubMember.getClub();

        ClubRoomInfo clubRoomInfo = chatRoomReadPort.findClubRoomInfoByClubId(club.getId()).orElseThrow();

        ChatRoom chatRoom = clubRoomInfo.getChatRoom();

        chatRoom.addParticipant(member);
    }
}
