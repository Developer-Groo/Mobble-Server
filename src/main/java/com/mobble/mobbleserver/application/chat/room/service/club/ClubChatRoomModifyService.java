package com.mobble.mobbleserver.application.chat.room.service.club;

import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomJoinPort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomType;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.chat.chatRoom.repository.ChatRoomRepository;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.repository.ClubChatRoomRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.validator.ClubChatRoomValidator;
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

    private final ClubMemberValidator clubMemberValidator;
    private final ClubChatRoomValidator clubChatRoomValidator;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Override
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        clubChatRoomValidator.existsClubChatRoomByClubIdOrThrow(club.getId());

        ChatRoom clubChatRoom = ChatRoom.createClubChatRoom(club);

        clubChatRoom.addParticipant(member);
        chatRoomRepository.save(clubChatRoom);

        return ClubChatRoomPreviewResponseDto.toDto(clubChatRoom, club, null, 0, null);
    }

    // Todo: 파라미터로 id 받도록 수정 필요
    @Override
    public void joinClubChatRoom(ClubMember clubMember) {
        Member member = clubMember.getMember();
        Club club = clubMember.getClub();
        ClubRoomInfo clubRoomInfo = club.getClubRoomInfo();
        ChatRoom chatRoom = clubRoomInfo.getChatRoom();

        if (!chatRoomParticipantRepository.existsByChatRoomIdAndMemberId(chatRoom.getId(), member.getId())) {
            chatRoom.addParticipant(member);
        }
    }
}
